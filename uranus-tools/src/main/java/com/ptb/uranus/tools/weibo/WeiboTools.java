package com.ptb.uranus.tools.weibo;


import com.alibaba.fastjson.JSON;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.schedule.utils.MongoUtils;
import com.ptb.uranus.sdk.UranusSdk;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import com.ptb.uranus.spider.weibo.bean.WeiboSearchAccount;
import org.apache.commons.io.FileUtils;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by eric on 16/2/25.
 */
public class WeiboTools {
    static WeiboSpider weiboSpider = new WeiboSpider();


    private static boolean insertWeiboAccoutToDatabase(String weiboID) {
        String collect = UranusSdk.i().collect(weiboID, CollectType.C_WB_M_S, new JustOneTrigger(new Date().getTime() + 3000), Priority.L3);
        if (collect != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void importWbArticle(String confPath) throws IOException {
        List<String> lines = FileUtils.readLines(new File(confPath));
        String successFilename = "./succee.log";
        String errorFilename = "./error.log";

        File success = new File(successFilename);
        File failed = new File((errorFilename));
        success.delete();
        failed.delete();
        int successNum = 0;
        int failedNum = 0;
        for (String line : lines) {
            System.out.println(String.format("正在导入第 [%d]个微博媒体 [%s],已成功 [%d],失败 [%d]", successNum + failedNum + 1, line, successNum, failedNum));
            try {
                insertWeiboAccoutToDatabase(line.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileUtils.write(failed, line + "\r\n", true);
            failedNum++;
            System.out.println(String.format("导入第 [%d]个微博媒体 [%s] 失败,已成功 [%d],失败 [%d]", successNum + failedNum, line, successNum, failedNum));
        }
    }

    public static Document convertWeiboToWeiboSearch(WeiboSearchAccount weiboSearchAccount) {
        SearchLibWbMedia searchLibWbMedia = new SearchLibWbMedia();
        searchLibWbMedia.setDescription(weiboSearchAccount.getIntroduction());
        searchLibWbMedia.setFans_number(weiboSearchAccount.getFans());
        searchLibWbMedia.setWeibo_number(weiboSearchAccount.getWeibo());
        searchLibWbMedia.setGender(weiboSearchAccount.getGender().equals("女") ? "f" : "m");
        searchLibWbMedia.setNick_name(weiboSearchAccount.getAccount());
        searchLibWbMedia.setTou_xiang(weiboSearchAccount.getHeadportrait());
        searchLibWbMedia.setUser_id(weiboSearchAccount.getWeiboId());
        searchLibWbMedia.setVerified_reason(weiboSearchAccount.getCompany());
        String type = "-1";
        if (weiboSearchAccount.getVertifyType().contains("个人")) {
            type = "0";
        } else if (weiboSearchAccount.getVertifyType().contains("机构")) {
            type = "1";
        }
        searchLibWbMedia.setVerified_type(type);
        return Document.parse(JSON.toJSONString(searchLibWbMedia));
    }


    public static void importSearchResult(String weiboName) {
        Optional<List<WeiboSearchAccount>> weiboSerachAccountByName = weiboSpider.getWeiboSerachAccountByName(weiboName);
        if (weiboSerachAccountByName.isPresent()) {
            MongoCollection<Document> collection = MongoUtils.instance.getCollection("uranus", "weiboMedia");
            Optional<WeiboSearchAccount> first = weiboSerachAccountByName.get().stream().filter(k -> k.getAccount().equals(weiboName)).findFirst();
            if (first.isPresent()) {
                Document document = convertWeiboToWeiboSearch(first.get());
                collection.updateOne(Filters.eq("user_id", first.get().getAccount()), new Document("$set", document), new UpdateOptions().upsert(true));
                System.out.println(String.format("导入微博媒体 name[%s] 成功", JSON.toJSONString(document)));
            }
        } else {
            System.out.println(String.format("没有找到该微博媒体 [%s] 失败", weiboName));
        }
    }
    public static void importToWbSearch() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String weiboName = scanner.next();
            importSearchResult(weiboName);
        }
    }

    public static void main(String[] args) {
    }
}
