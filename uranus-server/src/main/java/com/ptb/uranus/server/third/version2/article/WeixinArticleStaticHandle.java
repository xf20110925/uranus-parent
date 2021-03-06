package com.ptb.uranus.server.third.version2.article;

import com.jayway.jsonpath.JsonPath;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.entity.IdRecord;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;
import com.ptb.uranus.server.third.version2.DataHandle;
import com.ptb.uranus.server.third.version2.ReqUrlEnum;
import com.ptb.uranus.server.third.version2.SchedulerUpdater;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/2
 * @Time: 15:10
 */
public class WeixinArticleStaticHandle implements DataHandle {
	static Pattern bizRegex = Pattern.compile(".*_biz=([^&]*).*");

	private Sender sender;

    private SchedulerUpdater scheduleUpdater;

  	public WeixinArticleStaticHandle(Sender sender, SchedulerUpdater scheduleUpdater) {
		this.sender = sender;
		this.scheduleUpdater = scheduleUpdater;
 	}

  @Override
	public void handleBusEntities(String dataUrl) {
		String pageSource = HttpUtil.getPageSourceByClient(dataUrl);
		logger.info(String.format("[%d] [wx:static] pull from url [%s]", System.currentTimeMillis(), dataUrl));
		List<Map<String, String>> wxStaticAtricles = JsonPath.parse(pageSource).read("$.pages", List.class);
		wxStaticAtricles.stream().filter(map -> isExists(map.get("url"), bizRegex, 1)).map(ConvertUtils::convertWXArticleStatic).forEach(wxAS -> {
		  sender.sendArticleStatic(wxAS);
		  scheduleUpdater.add(new SchedulerUpdater.NewArticleScheduler(wxAS.getBiz(), wxAS.getPostTime()));
		});
	}

	@Override
	public long getStartId() {
		Optional<IdRecord> idRecordOpt = IdRecordUtil.getIdRecord();
		return idRecordOpt.orElse(new IdRecord()).getStaticArticleId();
	}

	@Override
	public void recordId(long maxId) {
		IdRecordUtil.syncStaticArticleId(maxId);
	}

	@Override
	public List<String> getRangeUrls() {
		List<Long> ids = getIds(ReqUrlEnum.WX_ARTICLE_STATIC_RANGE_URL.getValue(), 100);
		return ids.stream().map(id -> String.format(ReqUrlEnum.WX_ARTICLE_STATIC_DATA_URL.getValue(), id)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		KafkaBus bus = new KafkaBus();
		Sender sender = new BusSender(bus);
		bus.start(false, 5);
		new WeixinArticleStaticHandle(sender, null).handle();
	}
}
