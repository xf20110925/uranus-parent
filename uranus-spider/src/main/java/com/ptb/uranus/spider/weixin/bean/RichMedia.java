package com.ptb.uranus.spider.weixin.bean;

import java.util.List;

/**
 * Created by watsonzhang on 16/3/15.
 */
public class RichMedia {
    List<String> keyWords;
    List<String> imgs;
    List<String> vedios;
    List<String> audios;

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<String> getVedios() {
        return vedios;
    }

    public void setVedios(List<String> vedios) {
        this.vedios = vedios;
    }

    public List<String> getAudios() {
        return audios;
    }

    public void setAudios(List<String> audios) {
        this.audios = audios;
    }


}
