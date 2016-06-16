package com.ptb.uranus.tools.wechat;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.server.third.weixin.entity.BayouWXMedia;
import com.ptb.uranus.server.third.weixin.util.ConvertUtils;
import com.ptb.uranus.server.third.weixin.util.WeixinMediaUtils;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.GsData;
import com.ptb.uranus.spider.weixin.bean.WxAccount;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuefeng on 2016/6/15.
 */
public class WxMediaImport {
    public WxMediaImport(Sender sender) {
        this.sender = sender;
    }

    WeixinSpider weixinSpider = new WeixinSpider();
    Pattern pattern = Pattern.compile(".*名称：(.*)");
    Sender sender;

    private String formateAuthen(String authen) {
        if (StringUtils.isBlank(authen)) {
            return "";
        } else {
            Matcher matcher = pattern.matcher(authen);
            if (matcher.find()) {
                String group = matcher.group(1);
                if (StringUtils.isNotBlank(group)) return group;
            }
        }
        return "";
    }

    private Optional<BayouWXMedia> getMediaFromGSData(String wxId) {
        Optional<List<GsData>> wxMediaOpts = weixinSpider.getWeixinAccountByIdOrName(wxId, 1);
        BayouWXMedia bayouWXMedia = new BayouWXMedia();
        wxMediaOpts.ifPresent(wxMediaList -> {
            if (!wxMediaList.isEmpty()) {
                GsData gsData = wxMediaList.get(0);
                bayouWXMedia.setCode(gsData.getWechatid());
                bayouWXMedia.setInfo(gsData.getFunctionintroduce());
                bayouWXMedia.setName(gsData.getWechatname());
//                bayouWXMedia.setQrcode(gsData.getQrcodeurl());
                bayouWXMedia.setOriginal(false);
                bayouWXMedia.setAuthentication(formateAuthen(gsData.getAuthenticationInfo()));
                String articleUrl = gsData.getIncluded();
                if (StringUtils.isNotBlank(articleUrl)) {
                    String biz = RegexUtils.sub(".*__biz=([^#&]*).*", articleUrl, 0);
                    if (StringUtils.isNotBlank(biz)) {
                        bayouWXMedia.setBid(biz);
                        String qrCode = String.format("http://mp.weixin.qq.com/mp/qrcode?scene=10000004&size=102&__biz=%s", biz);
                        bayouWXMedia.setQrcode(qrCode);
                    }
                }
            }
        });
        if (StringUtils.isNotBlank(bayouWXMedia.getCode()) && wxId.equals(bayouWXMedia.getCode()))
            return Optional.of(bayouWXMedia);
        return Optional.empty();
    }

    private Optional<BayouWXMedia> getMediaFromSoGou(String wxId) {
        Optional<WxAccount> wxMediaOpt = weixinSpider.getWeixinAccountByWeixinID(wxId);
        BayouWXMedia bayouWXMedia = new BayouWXMedia();
        wxMediaOpt.ifPresent(wxAccount -> {
            bayouWXMedia.setCode(wxAccount.getId());
            bayouWXMedia.setInfo(wxAccount.getBrief());
            bayouWXMedia.setName(wxAccount.getNickName());
            bayouWXMedia.setQrcode(wxAccount.getQcCode());
            bayouWXMedia.setOriginal(false);
            bayouWXMedia.setAuthentication("");
            bayouWXMedia.setBid(wxAccount.getBiz());
        });
        if (StringUtils.isNotBlank(bayouWXMedia.getCode()) && wxId.equals(bayouWXMedia.getCode()))
            return Optional.of(bayouWXMedia);
        return Optional.empty();
    }

    private Optional<BayouWXMedia> getMediaByWXId(String wxId) {
        Optional<BayouWXMedia> mediaOpt = getMediaFromGSData(wxId);
        if (mediaOpt.isPresent()) return mediaOpt;
        return getMediaFromSoGou(wxId);
    }

    public void syncWXMedia(String wxId) {
        Optional<BayouWXMedia> mediaOpt = getMediaByWXId(wxId);
        mediaOpt.ifPresent(bayouWXMedia -> {
            System.out.println(" 媒体信息-->" + bayouWXMedia);
            WeixinMediaUtils.updateWeixinMedia(bayouWXMedia.getBid(), bayouWXMedia);
            WeixinMediaStatic weixinMediaStatic = ConvertUtils.convertWXMedia(bayouWXMedia);
            //发送到kafka
            sender.sendMediaStatic(weixinMediaStatic);
            System.out.println("媒体同步成功");
        });
    }

    public static void main(String[] args) {
        Bus bus = new KafkaBus();
        bus.start(false, 3);
        WxMediaImport wxMediaImport = new WxMediaImport(new BusSender(bus));
        Optional<BayouWXMedia> wxMedia = wxMediaImport.getMediaByWXId("prstorynew");
        wxMedia.ifPresent(System.out::print);
    }
}
