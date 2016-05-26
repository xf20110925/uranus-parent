package com.ptb.uranus.tools;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.sdk.UranusSdk;
import com.ptb.uranus.spider.weixin.mapping.WeixinLinkMap;
import com.ptb.uranus.tools.wechat.WxTools;
import com.ptb.uranus.tools.weibo.WeiboTools;

/**
 * Created by eric on 16/2/25.
 */
public class Tools {


    public static void main(String[] args) throws Exception {


        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("h", "help", false, "显示帮助信息");
        options.addOption("x", "import_wx", true, "参数为要导入的媒体文件,txt格式,每行一个微信号,从文件中加载微信媒体ID,并将对应的媒体添加到数据库中");
        options.addOption("import_wx_hotArticle", true, "从SOGOU中抽取指定数量的文章并加入了数据库中");
        options.addOption("b", "import_wb", true, "参数为要导入的媒体文件,txt格式,每行一个微博号[10位的数字],从文件中加载微博媒体ID,并将对应的媒体添加到数据库中");
        options.addOption("c", false, "建立SOGOU到WEIXIN文章的CACHE关系");
        options.addOption("sdk_tool",false, "sdk接口帮助文档");
        options.addOption("sdk_url",true,"指定url参数如果输入列表形式请以,分割");
        options.addOption("sdk_type",true,"采集类型： C_WX_M_D,C_WX_M_S,C_WX_A_D,C_WX_A_S,C_WB_A_S,C_WB_A_D,C_WB_M_D，C_WB_M_S，C_A_A_D，C_A_A_S，C_A_A_N，C_WB_A_N，C_WX_A_N");
        options.addOption("sdk_time",true,"程序执行时间戳，如果需要立即执行请输入1；");
        options.addOption("sdk_priority",true,"优先级：L1，L2，L3");
        CommandLine commandLine;
        HelpFormatter formatter = new HelpFormatter();
        try {
            commandLine = parser.parse(options, args);
        } catch (Exception e) {
            formatter.printHelp("usage:", options);
            e.printStackTrace();
            throw e;
        }
        if (commandLine.hasOption('h')) {
            formatter.printHelp("usage:", options);
            System.exit(-1);
        } else if (commandLine.hasOption("import_wx")) {
            String confPath = commandLine.getOptionValue("import_wx");
            WxTools.importWxMedia(confPath);
        } else if (commandLine.hasOption("import_wx_hotArticle")) {
            int saveArticleMaxCount = Integer.parseInt(commandLine.getOptionValue("import_wx_hotArticle", "10000"));
            WxTools.importWxArticle(saveArticleMaxCount);
        } else if (commandLine.hasOption("import_wb")) {
            String confPath = commandLine.getOptionValue("import_wb");
            WeiboTools.importWbArticle(confPath);
        } else if (commandLine.hasOption("c")) {
            WeixinLinkMap.cacheSogouToWeixin();
        }else if(commandLine.hasOption("sdk_tool")) {
        	String sdk_url = commandLine.getOptionValue("sdk_url");
        	String sdk_type = commandLine.getOptionValue("sdk_type");
        	Long sdk_time = Long.parseLong(commandLine.getOptionValue("sdk_time"));
        	String sdk_priority = commandLine.getOptionValue("sdk_priority");
        	String[] sdkL = sdk_url.split(",");
        	List<String> sdkList = new ArrayList<>();
        	JustOneTrigger time = new JustOneTrigger(sdk_time);
        	if(sdk_time==1){
    			time=new JustOneTrigger(new Date().getTime()); 
    		}
        	if(sdkL.length>1){
        		for(int i=0;i<sdkL.length;i++){
        			sdkList.add(sdkL[i]);
        		}
        	List<String> listurl= UranusSdk.i().collect(sdkList, CollectType.valueOf(sdk_type), time,Priority.valueOf(sdk_priority));
        	System.out.println(listurl);
        	}else{
                String name= UranusSdk.i().collect(sdk_url,  CollectType.valueOf(sdk_type), time, Priority.valueOf(sdk_priority));
     	        System.out.println(name);
        	}
        }else {
            formatter.printHelp("usage:", options);
        }


    }
}
