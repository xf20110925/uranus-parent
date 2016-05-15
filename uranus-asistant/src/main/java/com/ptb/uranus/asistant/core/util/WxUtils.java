package com.ptb.uranus.asistant.core.util;

import com.ptb.utils.exception.PTBException;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;


/**
 * Created by eric on 16/1/13.
 */
public class WxUtils {
    public static String getUrlKey(String url) {
        if (url == null) {
            return null;
        }
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", url, 0);
        String mid = RegexUtils.sub(".*mid=([^#&]*).*", url, 0);
        String sn = RegexUtils.sub(".*sn=([^#&]*).*", url, 0);

        if (url.contains("mp.weixin.qq.com/s")) {
            if (biz.length() > 0 && mid.length() > 0 && sn.length() > 0) {
                return String.format("A:%s:%s:%s", biz, mid, sn);
            }
        } else if (url.contains("getmasssendmsg")) {
            if (biz != null) {
                return String.format("L:%s", biz);
            }
        } else {
            return Md5Crypt.md5Crypt(url.getBytes());
        }

        throw new PTBException("不能获得文章的唯一标识");

    }

    public static String getUrlKeyV1(String url) {
        if (url == null) {
            return null;
        }
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", url, 0);
        String mid = RegexUtils.sub(".*mid=([^#&]*).*", url, 0);
        String sn = RegexUtils.sub(".*sn=([^#&]*).*", url, 0);

        if (url.contains("mp.weixin.qq.com/s")) {
            if (biz.length() > 0 && mid.length() > 0) {
                return String.format("A1:%s:%s:%s", biz, mid, sn);
            }
        } else if (url.contains("getmasssendmsg")) {
            if (biz != null) {
                return String.format("L1:%s", biz);
            }
        }
        throw new PTBException("不能获得文章的唯一标识");

    }

    /**
     * redis中存放带key的url
     *
     * @param url
     * @return
     */
    public static String getUrlKeyV2(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", url, 0);

        if (url.contains("mp.weixin.qq.com")) {
            if (biz.length() > 0) {
                return String.format("A2:%s", biz);
            }
        } else {
            return Md5Crypt.md5Crypt(url.getBytes());
        }

        throw new PTBException("不能获得文章的唯一标识");
    }

    public static void main(String[] args) {
        System.out.println(getUrlKeyV2("http://mp.weixin.qq.com/s?__biz=MjM5NzMxOTg4MA==&mid=2650320864&idx=6&sn=cef3f6b26710de2800c43cec4324461a&scene=4"));
    }
}
