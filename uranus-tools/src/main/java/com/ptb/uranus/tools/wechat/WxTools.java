package com.ptb.uranus.tools.wechat;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.gaia.bus.message.MessageListener;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.sdk.UranusSdk;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.GsData;
import com.ptb.uranus.spider.weixin.bean.PushMessage;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import com.ptb.uranus.spider.weixin.parse.WxPushMessageParser;
import com.ptb.utils.string.RegexUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class WxTools {

	public static void importWxMedia(String confPath) throws IOException, InterruptedException {
		String sogouCookie = "ABTEST=8|1458463628|v1; SUID=B1505A746A20900A0000000056EE638C; SNUID=CED025F48085510886F7856B975CF4; SUID=B1505A741508990A0000000056EE638D; SUV=1458463629362574; PHPSESSID=klekool09d819ajn2rh1e9lk65; SUIR=CED025F48085510886F7856B80975CF4; weixinIndexVisited=1; LSTMV=843%2C66; LCLKINT=111; seccodeErrorCount=1|Sun, 20 Mar 2016 14:04:50 GMT; refresh=1; IPLOC=CN1100";
		WeixinSpider weixinSpider = new WeixinSpider();

		String cookie = sogouCookie;

		List<String> lines = FileUtils.readLines(new File(confPath));
		int successCount = 0;
		int errorCount = 0;

		String successFilename = "./succee.log";
		String errorFilename = "./error.log";
		File success = new File(successFilename);
		File failed = new File((errorFilename));

		success.delete();
		failed.delete();
		int i = 0;
		for (String line : lines) {
			i++;
			try {
				System.out.println(String.format("正在进行导入第[%d]条....total[%d],success[%d],error[%d]", i, lines.size(), successCount, errorCount));
				Optional<GsData> aritcleUrl = weixinSpider.getWeixinAccountByIdOrName(line.trim(), 1).get().stream().filter(gsData -> gsData.getWechatid().equals(line.trim())).findFirst();
				String url = null;
				if (aritcleUrl.isPresent() && StringUtils.isNotBlank(aritcleUrl.get().getIncluded())) {
					url = aritcleUrl.get().getIncluded();
				} else {
					url = WxSogouParser.getLastArticleByWeixinID(line.trim(), cookie);
				}
				url = weixinSpider.convertSogouWeixinUrlToRealUrl(url);
				if (url != null) {
					UranusSdk.i().collect(url, CollectType.C_WX_M_S, new JustOneTrigger(new Date().getTime() + 3000), Priority.L3);
					System.out.println(String.format("insert wxID [%s] success", line));
					successCount++;
					FileUtils.writeStringToFile(success, line + "\r\n", true);
					continue;
				}
			} catch (Exception e) {
				System.out.println(String.format("add wxid[%s] error", line));
			}
			errorCount++;
			FileUtils.writeStringToFile(failed, line + "\r\n", true);
		}
		System.out.println(String.format("导入结束....total[%d],success[%d],error[%d]", lines.size(), successCount, lines.size() - successCount));
	}

	public static void importWxArticle(int saveArticleMaxCount) {
		WeixinSpider weixinSpider = new WeixinSpider();
		List<String> hotArticleFromSogou = weixinSpider.getHotArticleFromSogou().parallelStream().filter(url -> url.contains("mp.weixin.qq.com")).collect(Collectors.toList());
		UranusSdk.i().collect(hotArticleFromSogou, CollectType.C_WX_M_S, new JustOneTrigger(new Date().getTime() + 3000), Priority.L3);
	}

	public static void persistWxSourceLink() {
		Bus bus = new KafkaBus("source-link");
		bus.addRecvMessageListener(new MessageListener() {
			public void receive(Bus bus, Message msg) {
				Object sourceLink = msg.getBody();
				UranusSdk.i().collect((String) sourceLink, CollectType.C_WX_M_S, new JustOneTrigger(new Date().getTime() + 3000), Priority.L3);
			}

			public boolean match(Message message) {
				return message.getType() == 0L;
			}
		});
		bus.start(true, 1);
	}

	public static void syncWXMedia(String wxId) {
		System.out.println("starting....");
		Bus bus = new KafkaBus();
		bus.start(false, 3);
		WxMediaImport wxMediaImport = new WxMediaImport(new BusSender(bus));
		wxMediaImport.syncWXMedia(wxId);
	}


	public static void importToWxSearch() {
		KafkaBus bus = new KafkaBus("uranus-tool");
		bus.start(false, 0);
		BusSender busSender = new BusSender(bus);

		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			try {
				String weiboName = scanner.next();
				importSearchResult(weiboName, busSender);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void importSearchResult(String weixinName, BusSender busSender) {
		WeixinSpider weixinSpider = new WeixinSpider();
		Optional<List<GsData>> winxinMatchingByName = weixinSpider.getWinxinMatchingByName(weixinName, 1);
		if (winxinMatchingByName.isPresent()) {
			winxinMatchingByName.get().stream().filter(gsData -> gsData.getWechatname().equals(weixinName)).forEach(gsData -> {
				WeixinMediaStatic weixinMediaStatic = ConvertGsDataToWxMediaSender(gsData);
				if (weixinMediaStatic == null) {
					System.out.println(String.format("媒体历史无发文,无法导入[%s]", weixinName));
					return;
				} else {
					busSender.sendMediaStatic(weixinMediaStatic);
					System.out.println(String.format("导入微信媒体 name[%s] 成功", weixinMediaStatic.getMediaName()));
				}
			});
		} else {
			System.out.println(String.format("没有找到媒体名称[%s]", weixinName));
		}
	}

	private static WeixinMediaStatic ConvertGsDataToWxMediaSender(GsData gsData) {
		if (gsData.getIncluded() == null) {
			return null;
		}
		WeixinMediaStatic weixinMediaStatic = new WeixinMediaStatic();
		weixinMediaStatic.setMediaName(gsData.getWechatname());
		weixinMediaStatic.setAuthentication(gsData.getAuthenticationInfo().replaceAll(".*：", "").replace("暂未认证", ""));
		weixinMediaStatic.setBiz(RegexUtils.sub(".*__biz=([^&]+)&*", gsData.getIncluded(), 0));
		weixinMediaStatic.setHeadImg(gsData.getQrcodeurl());
		weixinMediaStatic.setQrCode(gsData.getQrcodeurl());
		weixinMediaStatic.setWeixinId(gsData.getWechatid());
		weixinMediaStatic.setBrief(gsData.getFunctionintroduce());
		weixinMediaStatic.setPlat(1);
		return weixinMediaStatic;
	}

	private static SearchLibWxMedia ConvertGsDataToSearchLibMedia(GsData gsData) {
		SearchLibWxMedia searchLibWxMedia = new SearchLibWxMedia();
		searchLibWxMedia.setAuthentication(gsData.getAuthenticationInfo().replaceAll(".*：", "").replace("暂未认证", ""));
		searchLibWxMedia.setBid(RegexUtils.sub(".*__biz=([^&]+)&*", gsData.getIncluded(), 0));
		searchLibWxMedia.setHeadImage(gsData.getQrcodeurl());
		searchLibWxMedia.setQrcode(gsData.getQrcodeurl());
		searchLibWxMedia.setCode(gsData.getWechatid());
		searchLibWxMedia.setName(gsData.getWechatname());
		searchLibWxMedia.setInfo(gsData.getFunctionintroduce());
		if (gsData.getIncluded() == null) {
			return null;
		}
		return searchLibWxMedia;
	}

	public static void crawleMediaHistoryArticle() throws IOException {
		WeixinSpider weixinSpider = new WeixinSpider();
		WxPushMessageParser wxPushMessageParser = new WxPushMessageParser();

		String key = "cf237d7ae24775e8913545f0769fc2cb66163adcc5ce6b644abd331ebcb66af2abfcb4fe18de6c2a37867efdb1218526259aec1c1e71c4a9&&";
		String uin = "MjI4NzM2NjQ3NQ%3D%3D";
		String biz = "MzA4MDE4OTYxNA==";
		Long fackid = 0L;
		boolean isDone = false;
		List<String> urls = new LinkedList<>();
		do {
			try {
				List<PushMessage> xx = wxPushMessageParser.getRecentPushList(biz, fackid, key, uin);
				PushMessage pushMessage = xx.get(xx.size() - 1);
				fackid = pushMessage.getComm_msg_info().getId();
				xx.stream().forEach(pushMessage1 -> {
					try {
						urls.add(StringEscapeUtils.unescapeHtml(pushMessage1.getApp_msg_ext_info().getContent_url()));
					}catch (Exception e) {

					}
					if(pushMessage.getApp_msg_ext_info() != null && pushMessage.getApp_msg_ext_info().getMulti_app_msg_item_list() != null) {
						pushMessage.getApp_msg_ext_info().getMulti_app_msg_item_list().stream().forEach(extMsgInfo -> {
							String url = StringEscapeUtils.unescapeHtml(extMsgInfo.getContent_url());
							urls.add(url);
							System.out.println(url);
						});
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				break;
			} try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!isDone); if (urls.size() > 0) {
			FileUtils.writeLines(new File("./articleList.txt"), urls, true);
		}
	}
	public static void crawleWeixinArticle() {
		WeixinSpider weixinSpider = new WeixinSpider();
		KafkaBus bus = new KafkaBus();
		bus.start(false,0);
		BusSender busSender = new BusSender(bus);

		Scanner scanner = new Scanner(System.in);
		int total = 0;
		final int[] success = {0,0};
		while(scanner.hasNext()) {
			String url = scanner.nextLine().trim();
			total ++;
			Optional<WxArticle> articleByUrl = weixinSpider.getArticleByUrl(url);
			int finalTotal = total;
			articleByUrl.ifPresent(wxArticle -> {
				success[0]++;
				busSender.sendArticleStatic(SendObjectConvertUtil.weixinArticleStaticConvert(wxArticle));
				int retryNum = 0;
				while(retryNum++ < 3) {
					Optional<ReadLikeNum> articleReadLikeNumByUrl = weixinSpider.getArticleReadLikeNumByUrl(wxArticle.getArticleUrl(), 15);
					if (articleReadLikeNumByUrl.isPresent()) {
						BasicArticleDynamic basicArticleDynamic = SendObjectConvertUtil.wxArticleDynamicConvert(articleReadLikeNumByUrl.get());
						basicArticleDynamic.setUrl(wxArticle.getArticleUrl());
						busSender.sendArticleDynamic(basicArticleDynamic);
						success[1]++;
						break;
					}
				}
				System.out.printf("run total [%d] static success [%d] dynamic success [%d] [%s]\n", finalTotal, success[0], success[1], articleByUrl.get().getArticleUrl());
			});

		}
	}

	public static void main(String[] args) throws IOException {
		crawleWeixinArticle();
	}
}