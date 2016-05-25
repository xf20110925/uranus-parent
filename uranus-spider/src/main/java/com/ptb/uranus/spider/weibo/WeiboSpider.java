package com.ptb.uranus.spider.weibo;

import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import com.ptb.uranus.spider.weibo.bean.WeiboSearchAccount;
import com.ptb.uranus.spider.weibo.parse.WeiboAccountParser;
import com.ptb.uranus.spider.weibo.parse.WeiboArticleParser;
import com.ptb.uranus.spider.weibo.parse.WeiboSearchAccountParser;
import com.ptb.utils.string.RegexUtils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/1/28. <br>
 * 微博爬取的统一对提供接口使用服务
 */
public class WeiboSpider {
    /**
     * The Logger.
     */
    static Logger logger = Logger.getLogger(WeiboSpider.class);
    /**
     * The Weibo account parser.
     */
    private WeiboAccountParser weiboAccountParser = new WeiboAccountParser();
    /**
     * The Weibo article parse.
     */
    private WeiboArticleParser weiboArticleParse = new WeiboArticleParser();
    /**
     * The Weibo
     */
    private WeiboSearchAccountParser weiboSa = new WeiboSearchAccountParser();

    /**
     * Gets weibo article  by article url. <br>
     * URl地址一般长这个模样 http://m.weibo.cn/2093778914/DlrJ04oXA <br>
     *
     * @param url the url  //要爬取的URL文章地址
     * @return the weibo article static by article url //返回WeiboArticle,如果成功则isPresent()为真,否则为假
     */
    public Optional<WeiboArticle> getWeiboArticleByArticleUrl(String url) {
        try {
            if (url.contains("weibo")) {
                WeiboArticle wbArticle = weiboArticleParse.getWeiboArticleByArticleUrl(url);
                if (wbArticle != null) {
                    return Optional.of(wbArticle);
                }
            }
        } catch (Exception e) {
            logger.info(String.format("get weibo article by url [%s]", url), e);
        }
        return Optional.empty();
    }
    
    
    
    /**
     * 微博人物搜索
     * @param accouontName
     * @return
     */
    public Optional<List<WeiboSearchAccount>> getWeiboSerachAccountByName(String accouontName){
    	try {
			return weiboSa.getWeiboSerachAccountByName(accouontName);
		} catch (Exception e) {
			 logger.warn(String.format("get weibo serach accountByName", accouontName), e);
		}
    	return Optional.empty();
    	
    }

    /**
     * Gets weibo account  by weibo id.
     * 微博ID 一般为URl数字部分 http://m.weibo.cn/3974469906
     *
     * @param weiboID the weibo id
     * @return the weibo static by media id
     */
    public Optional<WeiboAccount> getWeiboAccountByWeiboID(String weiboID) {
        try {
            String weiboHomeUrl = String.format("http://m.weibo.cn/u/%s/", weiboID);
            return Optional.of(weiboAccountParser.getWeiboAccount(weiboHomeUrl));
        } catch (Exception e) {
            logger.warn(String.format("get weibo article by url [%s]", weiboID), e);
        }
        return Optional.empty();
    }

    /**
     * Gets weibo account by home page.
     * 通过输入一个URL的主页获取微博的媒体信息
     *
     * @param homeUrl the home url
     * @return the weibo account by home page
     */
    public Optional<WeiboAccount> getWeiboAccountByHomePage(String homeUrl) {
        try {
            if (homeUrl.contains("weibo")) {
                if (homeUrl.contains("weibo.cn/u/")) {
                    return Optional.of(weiboAccountParser.getWeiboAccount(homeUrl));
                } else {
                    String weiboID = weiboAccountParser.getWeiboIDFromUserHomePage(homeUrl);
                    return getWeiboAccountByWeiboID(weiboID);
                }
            }
        } catch (Exception e) {
            logger.warn(String.format("get weibo article by url [%s]", homeUrl), e);
        }
        return Optional.empty();
    }

    /**
     * Gets recent articles by weibo id <br>
     * 会主动访问其主页,获取ContainerID <br>
     * 再通过ContainerID获取最近的文章 <br>
     *
     * @param mediaID     the media id
     * @param lastestTime the lastest time
     * @return the recent articles by weibo id
     */
    public Optional<ImmutablePair<Long, List<String>>> getRecentArticlesByWeiboID(String mediaID, Long lastestTime) {
        try {
            WeiboAccount weiboAccount = weiboAccountParser.getWeiboAccount(String.format(String.format("http://m.weibo.cn/u/%s", mediaID)));
            return this.getRecentArticlesByContainerID(weiboAccount.getContainerID(), lastestTime);
        } catch (Exception e) {
            logger.warn(String.format("get weibo recent article by mediaid [%s]", mediaID), e);
        }
        return Optional.empty();

    }

    /**
     * Gets recent articles by container id. 建议使用此接口,获取最新文章
     *
     * @param containerID the container id  //containerID
     * @param startTime   the start time
     * @return the recent articles by container id
     */
    public Optional<ImmutablePair<Long, List<String>>> getRecentArticlesByContainerID(String containerID, Long startTime) {
        try {
            ImmutablePair<Long, List<String>> articleList = weiboArticleParse.getWeiboRecentArticlesByContainerID(containerID, startTime);
            if (articleList != null) {
                return Optional.of(articleList);
            }
        } catch (Exception e) {
            logger.warn(String.format("get weibo recent article by containerID [%s]", containerID), e);
        }
        return Optional.empty();

    }


    public Optional<WeiboAccount> getWeiboAccountByArticleUrl(String articleUrl) {
        try {
            String mediaId = RegexUtils.sub(".*(?:weibo\\.com|m.weibo\\.cn)/([^/]*)/.*", articleUrl, 0);
            String mediaUrl = "http://m.weibo.cn/u/" + mediaId;
            Optional<WeiboAccount> weiboAccount = getWeiboAccountByHomePage(mediaUrl);
            return weiboAccount;
        } catch (Exception e) {
            logger.warn(articleUrl, e);
        }
        return Optional.empty();
    }

    public void close() {
    }
}
