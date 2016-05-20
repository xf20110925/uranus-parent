package com.ptb.uranus.server.bayoudata;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.server.bayoudata.entity.BayouWXArticleDynamic;
import com.ptb.uranus.server.bayoudata.entity.BayouWXArticleStatic;
import com.ptb.uranus.server.bayoudata.entity.IdRecord;
import com.ptb.uranus.server.bayoudata.entity.BayouWXMedia;
import com.ptb.uranus.server.bayoudata.util.ConvertUtils;
import com.ptb.uranus.server.bayoudata.util.IdRecordUtil;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by xuefeng on 2016/5/17.
 */
public class BayouWeixinSync {
    private static Logger logger = Logger.getLogger(BayouWeixinSync.class);

    private static final String RANGEURL = "http://weixindata.pullword.com:58600/%s/range?auth_usr=vip_yahoo";
    private static final String DATAURL = "http://weixindata.pullword.com:58600/%s/%d?auth_usr=vip_yahoo";
    private static final String MEDIAFEILD = "biz";
    private static final String STATICARTICLFEILD = "page";
    private static final String DYNAMICARTICLEFEILD = "click";

    Sender sender = null;

    public BayouWeixinSync(Sender sender) {
        this.sender = sender;
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

    private RangeId getRangeId(String url, String identify) {
        String rangeUrl = String.format(url, identify);
        String pageSource = HttpUtil.getPageSourceByClient(rangeUrl);
        DocumentContext parse = JsonPath.parse(pageSource);
        int minId = Integer.parseInt(parse.read("$.minid").toString());
        int maxId = Integer.parseInt(parse.read("$.maxid").toString());
        Optional<IdRecord> idRecord = IdRecordUtil.getIdRecord();
        RangeId rangeId = new RangeId(minId, maxId);
        idRecord.ifPresent(idRec -> {
            switch (identify) {
                case MEDIAFEILD:
                    if (idRec.getMediaId() > minId) rangeId.setMinId(idRec.getMediaId());
                    break;
                case STATICARTICLFEILD:
                    if (idRec.getStaticArticleId() > minId) rangeId.setMinId(idRec.getStaticArticleId());
                    break;
                case DYNAMICARTICLEFEILD:
                    if (idRec.getDynamicArticleId() > minId) rangeId.setMinId(idRec.getDynamicArticleId());
                    break;
            }
        });
        return rangeId;
    }

    private Optional<List<BayouWXMedia>> getRangeMedia(long minId) {
        String mediaDataUrl = null;
        try {
            mediaDataUrl = String.format(DATAURL, MEDIAFEILD, minId);
            String pageSource = HttpUtil.getPageSourceByClient(mediaDataUrl);
            List<BayouWXMedia> wxMedias = JSON.parseArray(JsonPath.parse(pageSource).read("$.bizs").toString(), BayouWXMedia.class);
//            List<WeixinMediaStatic> wxMediaStatics = wxMedias.stream().map(wxMedia -> ConvertUtils.convertWXMedia(wxMedia)).collect(Collectors.toList());
            return Optional.of(wxMedias);
        } catch (Exception e) {
            logger.error(String.format("get media info from url[%s] fail exception[%s]", mediaDataUrl, e));
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<List<BayouWXArticleStatic>> getRangeArticleStatic(long minId) {
        String articleStaticUrl = null;
        try {
            articleStaticUrl = String.format(DATAURL, STATICARTICLFEILD, minId);
            String pageSource = HttpUtil.getPageSourceByClient(articleStaticUrl);
            List<BayouWXArticleStatic> wxArticleStatics = JSON.parseArray(JsonPath.parse(pageSource).read("$.pages").toString(), BayouWXArticleStatic.class);
//            List<WeixinArticleStatic> wxArticleStatics = wxArcStatics.stream().map(wxArcStatic -> ConvertUtils.convertWXArticleStatic(wxArcStatic)).collect(Collectors.toList());
            return Optional.of(wxArticleStatics);
        } catch (Exception e) {
            logger.error(String.format("get articleStatic info from url[%s] fail exception[%s]", articleStaticUrl, e));
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<List<BayouWXArticleDynamic>> getRangeArticleDynamic(long minId) {
         String articleDynamicUrl = null;
        try {
            articleDynamicUrl = String.format(DATAURL,DYNAMICARTICLEFEILD , minId);
            String pageSource = HttpUtil.getPageSourceByClient(articleDynamicUrl);
            List<BayouWXArticleDynamic> wxArticleDynamics = JSON.parseArray(JsonPath.parse(pageSource).read("$.clicks").toString(), BayouWXArticleDynamic.class);
//            List<BasicArticleDynamic> wxArticleDynamics = wxArcDynamics.stream().map(wxArcDynamic -> ConvertUtils.convertWXArticleDynamic(wxArcDynamic)).collect(Collectors.toList());
            return Optional.of(wxArticleDynamics);
        } catch (Exception e) {
            logger.error(String.format("get articleDynamic info from url[%s] fail exception[%s]", articleDynamicUrl, e));
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void syncMedias() {
        RangeId rangeId = getRangeId(RANGEURL, MEDIAFEILD);
        long minId = rangeId.getMinId();
        long maxId = rangeId.getMaxId();
        for (long i = minId; i < maxId; i += 100) {
            Optional<List<BayouWXMedia>> wxMediasOpt = getRangeMedia(i);
            wxMediasOpt.ifPresent(wxMedias -> {
                wxMedias.forEach(wxMedia -> {
                    WeixinMediaStatic weixinMediaStatic = ConvertUtils.convertWXMedia(wxMedia);
                    //发送到kafka，更新媒体发现新文章中的媒体最新文章发文时间
                    sender.sendMediaStatic(weixinMediaStatic);

                });
            });
        }
        IdRecordUtil.syncMediaId(maxId);


        return;
    }

    public void syncArticleStatics() {
        RangeId rangeId = getRangeId(RANGEURL, STATICARTICLFEILD);
        long minId = rangeId.getMinId();
        long maxId = rangeId.getMaxId();
        for (long i = minId; i < maxId; i += 100) {
            Optional<List<BayouWXArticleStatic>> wxArticlesOpt = getRangeArticleStatic(i);
            wxArticlesOpt.ifPresent(wxArticles -> {
                wxArticles.forEach(wxArticle -> {
                    //发送到kafka，更新媒体发现新文章中的媒体最新文章发文时间
                });
            });
            return;
        }
        IdRecordUtil.syncStaticArticleId(maxId);


        return;
    }

    public void syncArticleDynamics() {
        RangeId rangeId = getRangeId(RANGEURL, DYNAMICARTICLEFEILD);
        long minId = rangeId.getMinId();
        long maxId = rangeId.getMaxId();
        for (long i = minId; i < maxId; i += 100) {
            Optional<List<BayouWXArticleDynamic>> articleDynamicsOpt = getRangeArticleDynamic(i);
            articleDynamicsOpt.ifPresent(articleDynamics -> {
                articleDynamics.forEach(articleDynamic -> {

                     //发送到kafka，更新媒体发现新文章中的媒体最新文章发文时间
                });
            });
            return;
        }
        IdRecordUtil.syncDynamicArticleId(maxId);


        return;
    }



    public static void main(String[] args) {
        new BayouWeixinSync(null).syncArticleDynamics();
    }
}
