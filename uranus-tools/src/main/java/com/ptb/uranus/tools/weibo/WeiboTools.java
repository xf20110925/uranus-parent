package com.ptb.uranus.tools.weibo;


import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.sdk.UranusSdk;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
}
