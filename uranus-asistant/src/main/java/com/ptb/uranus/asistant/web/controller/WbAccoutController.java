package com.ptb.uranus.asistant.web.controller;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.asistant.core.entity.JSONResult;
import com.ptb.uranus.asistant.web.model.RWbAcctMeta;
import com.ptb.uranus.asistant.web.server.WbAccountService;
import com.ptb.uranus.asistant.web.service.WxService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by eric on 15/11/21.
 */
@Controller
public class WbAccoutController {
    static Logger logger = LoggerFactory.getLogger(WbAccoutController.class);


    @Resource
    WxService wxService;


    @Resource
    WbAccountService wbAccountService;

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public String AsisErrorHandler(Exception e) {
        return JSON.toJSONString(JSONResult.newErrorResult(e.getMessage()));
    }

    @ResponseBody
    @RequestMapping("/weibo/account/add")
    public String addWeiboAccount(String username,String password) {
        wbAccountService.addWeiboCount(username,password);
        return new JSONResult(null).toJson();
    }

    @ResponseBody
    @RequestMapping("/weibo/account/del")
    public String delWeiboAccount(String username) {
        wbAccountService.delWeiboCount(username);
        return new JSONResult<>().toJson();
    }

    @ResponseBody
    @RequestMapping("/weibo/valid/get")
    public String getWeiboAccountCookie() {
        RWbAcctMeta validWbAccount = wbAccountService.getValidWbAccount();
        return new JSONResult<RWbAcctMeta>(validWbAccount).toJson();
    }

    @RequestMapping("/weibo/account/volidateCode")
    public String getWeiboVolidateCode(Model model) {
        RWbAcctMeta rWbAcctMeta = wbAccountService.getInValidWbAccount();
        model.addAttribute("wbaccout",rWbAcctMeta);
        return "weibo/test.jsp";
    }


    public String setWeiboVolidateCode(String username,String volidateCookie) {
        wbAccountService.setWeiboVolidatCode(username,volidateCookie);
        return null;
    }

}
