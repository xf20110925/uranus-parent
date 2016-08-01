package com.ptb.uranus.server.third.weixin;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.server.third.weixin.entity.BayouWXArticleDynamic;
import com.ptb.uranus.server.third.weixin.entity.BayouWXMedia;
import com.ptb.uranus.server.third.weixin.entity.IdRecord;
import com.ptb.uranus.server.third.exception.BayouException;
import com.ptb.uranus.server.third.weixin.util.ConvertUtils;
import com.ptb.uranus.server.third.weixin.util.IdRecordUtil;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.util.*;



/**
 * Created by xuefeng on 2016/5/17.
 */
public class BayouWeixinSync {
    private static Logger requestLogger = Logger.getLogger("bayou.request");

    private static String RANGEURL = null;
    private static String DATAURL = null;
    private static final String MEDIAFEILD = "biz";
    private static final String STATICARTICLEFEILD = "page";
    private static final String DYNAMICARTICLEFEILD = "click";
    private static int TRYNUM = 3;

    Sender sender;
    //存放媒体biz和postTime键值对
    Map<String, Long> mediaMap = null;

    private void loadConfig() throws ConfigurationException {
        PropertiesConfiguration conf = new PropertiesConfiguration("uranus.properties");
        RANGEURL = conf.getString("uranus.bayou.range.url", "http://weixindata.pullword.com:12345/%s/range?auth_usr=pintuibao");
        DATAURL = conf.getString("uranus.bayou.data.url", "http://weixindata.pullword.com:12345/%s/%d?auth_usr=pintuibao");
    }

    public BayouWeixinSync(Sender sender) {
        try {
            loadConfig();
            //wxSceduleService = new WeixinScheduleService();
        } catch (ConfigurationException e) {

        }
        this.sender = sender;
        mediaMap = new HashMap<>();
    }

    private synchronized void addMedia(String mediaBiz, long nextPostTime) {
        if (mediaMap.containsKey(mediaBiz)) {
            Long lastPostTime = mediaMap.get(mediaBiz);
            if (nextPostTime > lastPostTime) {
                mediaMap.put(mediaBiz, nextPostTime);
            }
        } else {
            mediaMap.put(mediaBiz, nextPostTime);
        }
    }

    class RangeId {

        private long minId;
        private long maxId;

        public RangeId() {
        }

        public RangeId(long minId, long maxId) {
            this.minId = minId;
            this.maxId = maxId;
        }

        public long getMinId() {
            return minId;
        }

        public void setMinId(long minId) {
            this.minId = minId;
        }

        public long getMaxId() {
            return maxId;
        }

        public void setMaxId(long maxId) {
            this.maxId = maxId;
        }
    }

    /**
     * 获取rangId重试次数为三次
     *
     * @param rangeUrl
     * @return
     */
    private RangeId getRangeId(String rangeUrl, int tryNum) {
        while (tryNum-- > 0) {
            String pageSource = null;
            try {
                pageSource = HttpUtil.getPageSourceByClient(rangeUrl);
                DocumentContext parse = JsonPath.parse(pageSource);
                int minId = parse.read("$.minid", Integer.class);
                int maxId = parse.read("$.maxid", Integer.class);
                return new RangeId(minId, maxId);
            } catch (Exception e) {
                requestLogger.error(String.format("request url[%s] error response data[%s] exception[%s] ", rangeUrl, pageSource, e));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        throw new BayouException(String.format("get rangeId from url[%s] error", rangeUrl));
    }

    private RangeId getRangeId(String url, String identify) {
        String rangeUrl = String.format(url, identify);
        RangeId rangeId = getRangeId(rangeUrl, TRYNUM);
        long minId = rangeId.getMinId();
        Optional<IdRecord> idRecord = IdRecordUtil.getIdRecord();
        idRecord.ifPresent(idRec -> {
            switch (identify) {
                case MEDIAFEILD:
                    if (idRec.getMediaId() > minId) rangeId.setMinId(idRec.getMediaId());
                    break;
                case STATICARTICLEFEILD:
                    if (idRec.getStaticArticleId() > minId) rangeId.setMinId(idRec.getStaticArticleId());
                    break;
                case DYNAMICARTICLEFEILD:
                    if (idRec.getDynamicArticleId() > minId) rangeId.setMinId(idRec.getDynamicArticleId());
                    break;
                default:
                    break;
            }
        });
        return rangeId;
    }

    private Optional<List<BayouWXMedia>> getRangeMedia(String mediaDataUrl) {
        String pageSource = null;
        try {
            pageSource = HttpUtil.getPageSourceByClient(mediaDataUrl);
            List<BayouWXMedia> wxMedias = JSON.parseArray(JsonPath.parse(pageSource).read("$.bizs").toString(), BayouWXMedia.class);
            return Optional.of(wxMedias);
        } catch (Exception e) {
            requestLogger.error(String.format("get media info from url[%s] error response data[%s] exception[%s]", mediaDataUrl, pageSource, e));
        }
        return Optional.empty();
    }

    private Optional<List<Map<String, String>>> getRangeArticleStatic(String articleStaticUrl) {
        String pageSource = null;
        try {
            pageSource = HttpUtil.getPageSourceByClient(articleStaticUrl);
            List wxArticleStatics = JsonPath.parse(pageSource).read("$.pages", List.class);
            return Optional.of(wxArticleStatics);
        } catch (Exception e) {
            requestLogger.error(String.format("get article static from url[%s] error response data[%s] exception[%s]", articleStaticUrl, pageSource, e));
        }
        return Optional.empty();
    }

    private Optional<List<BayouWXArticleDynamic>> getRangeArticleDynamic(String articleDynamicUrl) {
        String pageSource = null;
        try {
            pageSource = HttpUtil.getPageSourceByClient(articleDynamicUrl);
            List<BayouWXArticleDynamic> wxArticleDynamics = JSON.parseArray(JsonPath.parse(pageSource).read("$.clicks").toString(), BayouWXArticleDynamic.class);
            return Optional.of(wxArticleDynamics);
        } catch (Exception e) {
            requestLogger.error(String.format("get article dynamic from url[%s] error response data[%s] exception[%s]", articleDynamicUrl, pageSource, e));
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void sendMedias(Optional<List<BayouWXMedia>> wxMediasOpt) {
        wxMediasOpt.ifPresent(wxMedias -> {
            wxMedias.stream().forEach(wxMedia -> {
                //WeixinMediaUtils.updateWeixinMedia(wxMedia.getBid(), wxMedia);
                WeixinMediaStatic weixinMediaStatic = ConvertUtils.convertWXMedia(wxMedia);
                //发送到kafka
                sender.sendMediaStatic(weixinMediaStatic);
            });
        });
    }

    public void syncMedias() {
        RangeId rangeId = getRangeId(RANGEURL, MEDIAFEILD);
        long minId = rangeId.getMinId();
        long maxId = rangeId.getMaxId();
        //存放请求失败的url
        Queue<String> mediaQueue = new LinkedList<>();
        String mediaDataUrl = null;
        if(minId > maxId) return;
        for (long i = minId; i <= maxId; i += 100) {
            mediaDataUrl = String.format(DATAURL, MEDIAFEILD, i);
            Optional<List<BayouWXMedia>> wxMediasOpt = getRangeMedia(mediaDataUrl);
            if (wxMediasOpt.isPresent()) {
                sendMedias(wxMediasOpt);
            } else {
                mediaQueue.add(mediaDataUrl);
            }
            IdRecordUtil.syncMediaId(i + 100);
        }
        //重试失败的url
        if (!mediaQueue.isEmpty()) {
            mediaQueue.forEach(url -> {
                try {
                    Optional<List<BayouWXMedia>> rangeMediasOpt = getRangeMedia(url);
                    sendMedias(rangeMediasOpt);
                } catch (Exception e) {
                }
            });
        }
    }

    private void sendArticleStatics(Optional<List<Map<String, String>>> wxArticlesOpt) {
        wxArticlesOpt.ifPresent(wxArticles -> {
            wxArticles.parallelStream().forEach(wxArticle -> {
                //发送到kafka，更新媒体发现新文章中的媒体最新文章发文时间
                WeixinArticleStatic wxArticleStatic = ConvertUtils.convertWXArticleStatic(wxArticle);
                sender.sendArticleStatic(wxArticleStatic);
                addMedia(wxArticleStatic.getBiz(), wxArticleStatic.getPostTime());
            });
        });
    }

    public void syncArticleStatics() {
        RangeId rangeId = getRangeId(RANGEURL, STATICARTICLEFEILD);
        long minId = rangeId.getMinId();
        long maxId = rangeId.getMaxId();
        Queue<String> staticsQueue = new LinkedList<>();
        String articleStaticUrl = null;
        if(minId > maxId) return;
        for (long i = minId; i <= maxId; i += 100) {
            articleStaticUrl = String.format(DATAURL, STATICARTICLEFEILD, i);
            Optional<List<Map<String, String>>> wxArticleStaticsOpt = getRangeArticleStatic(articleStaticUrl);
            if (wxArticleStaticsOpt.isPresent()) {
                sendArticleStatics(wxArticleStaticsOpt);
            } else {
                staticsQueue.add(articleStaticUrl);
            }
            IdRecordUtil.syncStaticArticleId(i + 100);
        }
        if (!staticsQueue.isEmpty()) {
            staticsQueue.forEach(url -> {
                Optional<List<Map<String, String>>> rangeArticleStatics = getRangeArticleStatic(url);
                sendArticleStatics(rangeArticleStatics);
            });
        }
        //更新媒体最新发文时间
       // mediaMap.forEach((k, v) -> wxSceduleService.updateWeixinMediaCondition(k, v));
    }

    private void sendArticleDynamics(Optional<List<BayouWXArticleDynamic>> articleDynamicsOpt) {
        articleDynamicsOpt.ifPresent(articleDynamics -> {
            articleDynamics.forEach(articleDynamic -> {
                BasicArticleDynamic basicArticleDynamic = ConvertUtils.convertWXArticleDynamic(articleDynamic);
                //发送到kafka
                sender.sendArticleDynamic(basicArticleDynamic);
            });
        });
    }

    public void syncArticleDynamics() {
        RangeId rangeId = getRangeId(RANGEURL, DYNAMICARTICLEFEILD);
        long minId = rangeId.getMinId();
        long maxId = rangeId.getMaxId();
        Queue<String> dynamicsQueue = new LinkedList<>();
        String articleDynamicUrl = null;
        if (minId > maxId) return;
        for (long i = minId; i <= maxId; i += 100) {
            articleDynamicUrl = String.format(DATAURL, DYNAMICARTICLEFEILD, i);
            Optional<List<BayouWXArticleDynamic>> articleDynamicsOpt = getRangeArticleDynamic(articleDynamicUrl);
            if (articleDynamicsOpt.isPresent()) {
                sendArticleDynamics(articleDynamicsOpt);
            } else {
                dynamicsQueue.add(articleDynamicUrl);
            }
            IdRecordUtil.syncDynamicArticleId(i + 100);
        }
        if (!dynamicsQueue.isEmpty()) {
            dynamicsQueue.forEach(url -> {
                Optional<List<BayouWXArticleDynamic>> rangeArticleStatics = getRangeArticleDynamic(url);
                sendArticleDynamics(rangeArticleStatics);
            });
        }
    }

    private Map<String, Long> getMediaMap() {
        return mediaMap;
    }

    public static void main(String[] args) throws ConfigurationException {
        Bus bus = new KafkaBus();
        bus.start(false, 3);
        BayouWeixinSync bayouWeixinSync = new BayouWeixinSync(new BusSender(bus));
        bayouWeixinSync.syncMedias();
//        bayouWeixinSync.syncArticleDynamics();
//        bayouWeixinSync.syncArticleStatics();
    }
}
