package com.ptb.uranus.server.third.weibo.script;

import com.ptb.ourea.function.TextAnalyzeResult;
import com.ptb.ourea.function.TextAnalyzer;
import com.ptb.ourea.function.TextAnalyzerFactory;
import com.ptb.ourea.function.TextAnalyzerFactory.AnalyzerType;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.WeiboArticleStatic;
import com.ptb.uranus.server.send.entity.media.BasicMediaDynamic;
import com.ptb.uranus.server.send.entity.media.WeiboMediaDynamic;
import com.ptb.uranus.server.send.entity.media.WeiboMediaStatic;
import com.ptb.uranus.server.third.exception.BayouException;
import com.ptb.uranus.server.third.weibo.entity.FreshData;
import com.ptb.uranus.server.third.weibo.entity.UserProfile;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/7/30
 * @Time: 20:57
 */
public class ConvertUtils {
	private static TextAnalyzer textAnalyzer;

	static {
		try {
			textAnalyzer = TextAnalyzerFactory.create(AnalyzerType.ANJS);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static WeiboArticleStatic weiboArticleStaticConvert(FreshData freshData) {
		TextAnalyzeResult textAnalyzeResult;
		WeiboArticleStatic weiboArticleStatic = null;
		try {
			textAnalyzeResult = textAnalyzer.ArticleAnalyze(freshData.getWeibo_content());
			weiboArticleStatic = new WeiboArticleStatic();
			weiboArticleStatic.setWeiboId(freshData.getWeibo_id());
			weiboArticleStatic.setKeywords(textAnalyzeResult.getAbstracts());
			weiboArticleStatic.setTitle(textAnalyzeResult.getArticle());
			weiboArticleStatic.setAuthor(freshData.getNick_name());
			weiboArticleStatic.setClassify("");
			weiboArticleStatic.setContent(freshData.getWeibo_content());
			if (weiboArticleStatic.getContent().contains("vodeo_defalut.png") || weiboArticleStatic.getContent().contains("http://video.weibo.com") || weiboArticleStatic.getContent().contains("miaopai.")) {
				weiboArticleStatic.setType("video");
			} else if (weiboArticleStatic.getContent().contains("http://huati.weibo")) {
				weiboArticleStatic.setType("topic");
			} else if (weiboArticleStatic.getContent().contains("vote_default.png")) {
				weiboArticleStatic.setType("vote");
			} else if (weiboArticleStatic.getContent().contains("music_defalut.png")) {
				weiboArticleStatic.setType("audio");
			} else {
				weiboArticleStatic.setType("article");
			}
			String pic = freshData.getPic_content();
			if (StringUtils.isNotBlank(pic)) {
				String[] picArray = pic.split(",");
				for (int i = 0; i < picArray.length; i++) {
					if (picArray[i].length() < 30) {
						continue;
					}
					String imgUrl = String.format("http://ww4.sinaimg.cn/thumb180/%s.jpg", picArray[i]);
					if (StringUtils.isBlank(weiboArticleStatic.getPicture())) {
						weiboArticleStatic.setPicture(imgUrl);
					}
					textAnalyzeResult.getHyperLink().put("", imgUrl);
				}
			} else {
				textAnalyzeResult.getHyperLink().put("", "");
			}


			weiboArticleStatic.setLinks(textAnalyzeResult.getHyperLink());
			weiboArticleStatic.setPlat(2);
			weiboArticleStatic.setPostTime(Long.parseLong(freshData.getTime_stamp()));
			weiboArticleStatic.setSource(freshData.getDevice());
			weiboArticleStatic.setSplitwords(textAnalyzeResult.getSplitword());
			weiboArticleStatic.setOriginal(freshData.getIs_retweet() == 1);

			//			String url = String.format("http://m.weibo.cn/%s/%s", .getString("user_id"), WeiboUtil.mid2url(rs.getString("weibo_id")));
			String url = freshData.getUrl();
			weiboArticleStatic.setUrl(url);

		} catch (Exception e) {
			throw new BayouException("第三方微博文章静态数据转换异常");
		}

		return weiboArticleStatic;
	}

	public static BasicArticleDynamic weiboArticleDynamicConvert(FreshData freshData) {
		BasicArticleDynamic wbArticleDynamic = new BasicArticleDynamic();
		try {
			//            String url = String.format("http://m.weibo.cn/%s/%s", rs.getString("user_id"), WeiboUtil.mid2url(rs.getString("weibo_id")));
			String url = freshData.getUrl();
			wbArticleDynamic.setUrl(url);
			wbArticleDynamic.setComments(freshData.getPing());
			wbArticleDynamic.setLikes(freshData.getZhan());
			wbArticleDynamic.setForwards(freshData.getZhan());
			wbArticleDynamic.setPlat(2);
			wbArticleDynamic.setTime(freshData.getCrawler_time_stamp());
		} catch (Exception e) {
			throw new BayouException("第三方微博文章动态数据转换异常");
		}
		return wbArticleDynamic;
	}

	public static WeiboMediaStatic weiboMediaStaticConvert(UserProfile userProfile) {
		WeiboMediaStatic weiboMediaStatic = new WeiboMediaStatic();
		try {
			weiboMediaStatic.setWeiboId(userProfile.getUser_id());
			weiboMediaStatic.setPlat(2);
			weiboMediaStatic.setMediaName(userProfile.getNick_name());
			weiboMediaStatic.setAuthType(userProfile.getVerified_type());
			weiboMediaStatic.setAuthDescription(userProfile.getVerified_reason());
			weiboMediaStatic.setBrief(userProfile.getDescription());
			weiboMediaStatic.setGender(userProfile.getGender());
			weiboMediaStatic.setHeadImg(userProfile.getTou_xiang());
			weiboMediaStatic.setHomePage("http://m.weibo.cn/u/" + userProfile.getUser_id());
			weiboMediaStatic.setLocation(""); //???
			weiboMediaStatic.setRegisterTime(0);
			weiboMediaStatic.setTagList("");
		} catch (Exception e) {
			throw new BayouException("第三方微博媒体静态数据转换异常");
		}
		return weiboMediaStatic;
	}

	public static BasicMediaDynamic weiboMediaDynamicConvert(UserProfile userProfile) {
		WeiboMediaDynamic weiboMediaDynamic = new WeiboMediaDynamic();
		try {
			weiboMediaDynamic.setTime(System.currentTimeMillis());
			weiboMediaDynamic.setWeiboId(userProfile.getUser_id());
			weiboMediaDynamic.setPlat(2);
			weiboMediaDynamic.setFans(userProfile.getFans_number());
			weiboMediaDynamic.setPostArticles(userProfile.getWeibo_number());
			weiboMediaDynamic.setConcerns(0);
		} catch (Exception e) {
			throw new BayouException("第三方微博媒体动态数据转换异常");
		}
		return weiboMediaDynamic;
	}

}
