package com.ptb.uranus.tools.wechat;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.gaia.bus.message.MessageListener;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.schedule.utils.MongoUtils;
import com.ptb.uranus.sdk.UranusSdk;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.GsData;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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

    public static void main(String[] args) {
        importToWxSearch();
    }


    public static void importToWxSearch() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String weiboName = scanner.next();
            importSearchResult(weiboName);
        }
    }

    private static void importSearchResult(String weixinName) {
        WeixinSpider weixinSpider = new WeixinSpider();
        MongoCollection<Document> collection = MongoUtils.instance.getCollection("uranus", "weixinMedia");
        Optional<List<GsData>> winxinMatchingByName = weixinSpider.getWinxinMatchingByName(weixinName, 1);
        if (winxinMatchingByName.isPresent()) {
            winxinMatchingByName.get().stream().filter(gsData -> gsData.getWechatname().equals(weixinName)).forEach(gsData -> {
                SearchLibWxMedia searchLibWxMedia = ConvertGsDataToSearchLibMedia(gsData);
                if (searchLibWxMedia == null) {
                    return;
                }else {
                    collection.updateOne(Filters.eq("bid", searchLibWxMedia.getBid()), new Document("$set", Document.parse(JSON.toJSONString(searchLibWxMedia))), new UpdateOptions().upsert(true));
                    System.out.println(String.format("导入微信媒体 name[%s] 成功", JSON.toJSONString(searchLibWxMedia)));
                }
            });
        }
        System.out.println(String.format("没有找到媒体名称[%s]", weixinName));

    }

    private static SearchLibWxMedia ConvertGsDataToSearchLibMedia(GsData gsData) {
        SearchLibWxMedia searchLibWxMedia = new SearchLibWxMedia();
        searchLibWxMedia.setAuthentication(gsData.getAuthenticationInfo().replaceAll(".*：","").replace("暂未认证",""));
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

}