package com.ptb.uranus.server.send.entity.article;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eric on 16/4/25.
 */
public class BasicArticleStatic {
    String url;
    int plat;
    String author;
    String source;
    String classify;
    Long postTime;
    String title;
    String content;
    List<String> keywords;
    List<String> splitwords;
    HashMap<String,String> links;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlat() {
        return plat;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public Long getPostTime() {
        return postTime;
    }

    public void setPostTime(Long postTime) {
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getSplitwords() {
        return splitwords;
    }

    public void setSplitwords(String[] splitwords) {
        this.splitwords = Arrays.asList(splitwords);
    }

    public HashMap<String, String> getLinks() {
        return links;
    }

    public void setLinks(HashMap<String, String> links) {
        this.links = links;
    }
}
