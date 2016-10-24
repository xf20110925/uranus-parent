package com.ptb.uranus.spider.temp;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by eric on 16/9/13.
 */
public class OpenEyeSpider {
	static class WebUrl {
		String raw;
		String forWeibo;

		public String getRaw() {
			return raw;
		}

		public void setRaw(String raw) {
			this.raw = raw;
		}

		public String getForWeibo() {
			return forWeibo;
		}

		public void setForWeibo(String forWeibo) {
			this.forWeibo = forWeibo;
		}
	}

	static class Tag {
		String id;
		String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	static class ItemData {
		int id;
		String title;
		String description;
		String category;
		String author;
		String playUrl;
		WebUrl webUrl;
		List<Tag> tags;

		public WebUrl getWebUrl() {
			return webUrl;
		}

		public void setWebUrl(WebUrl webUrl) {
			this.webUrl = webUrl;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getPlayUrl() {
			return playUrl;
		}

		public void setPlayUrl(String playUrl) {
			this.playUrl = playUrl;
		}

		public List<Tag> getTags() {
			return tags;
		}

		public void setTags(List<Tag> tags) {
			this.tags = tags;
		}
	}
	static class Item {
		String type;
		ItemData data;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public ItemData getData() {
			return data;
		}

		public void setData(ItemData data) {
			this.data = data;
		}
	}
	static class OpenEyeArticleOBJ {
		List<Item> itemList;

		public List<Item> getItemList() {
			return itemList;
		}

		public void setItemList(List<Item> itemList) {
			this.itemList = itemList;
		}

	}
	static String sDataTemplateUrl = "http://baobab.wandoujia.com/api/v3/videos?start=%d&num=20&categoryId=%d&strategy=date&_s=2191a924533d44b285397f00e57355a9&f=iphone&net=wifi&u=0700d57ed7c7fd73da2b19835bb9844514ad70b9&v=2.6.0&vc=1209";
	public static OpenEyeArticleOBJ parseOpenEyeArticle(String page) {
		OpenEyeArticleOBJ openEyeArticleOBJ = JSON.parseObject(page, OpenEyeArticleOBJ.class);
		return openEyeArticleOBJ;
	}
	public static void main(String[] args) throws IOException {
		FileWriter fileWriter  = null;
		try {
			fileWriter = new FileWriter("./OpenEye.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}


		for(int j = 0; j< 40;j=j+2) {
			for (int i = 0; i < 10000; i++) {
				try {
					String pageString = HttpUtil.getPageSourceByClient(String.format(sDataTemplateUrl, i * 20, j));
					OpenEyeArticleOBJ openEyeArticleOBJ = parseOpenEyeArticle(pageString);
					if (openEyeArticleOBJ.getItemList().size() == 0) {
						break;
					}
					System.out.println(i);
					for (Item item : openEyeArticleOBJ.itemList) {
						fileWriter.append(String.format("%d\t%s\t%s\t%s\t%s\n", j,item.getData().getWebUrl().getRaw(), item.getData().getPlayUrl(), item.getData().getTitle(), item.getData().getCategory()));
						fileWriter.flush();
					}
				}catch (Exception e) {
					break;
				}
			}
		}
		fileWriter.close();

	}
}
