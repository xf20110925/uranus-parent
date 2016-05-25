package com.ptb.uranus.asistant.web.controller;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.asistant.core.entity.JSONResult;
import com.ptb.uranus.asistant.web.model.rep.RequestUrlResponse;
import com.ptb.uranus.asistant.web.service.WxService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by eric on 15/11/21.
 */
@Controller
public class WxController {
//    static Logger logger = Logger.getLogger(WxController.class);


    @Resource
    WxService wxService;


    @ExceptionHandler({Exception.class})
    @ResponseBody
    public String AsisErrorHandler(Exception e) {
        return JSON.toJSONString(JSONResult.newErrorResult(e.getMessage()));
    }

    @RequestMapping(value = "wx/mobile/getNewUrl")
    @ResponseBody
    public String getNewUrl(HttpServletRequest request, @RequestParam("callback") String callback,
                            @RequestParam("data") String data) {
        try {
            JSONResult jsonResult = new JSONResult();

            if (data != null) {
                String url = JsonPath.parse(data).read("$.url");
                if (url.contains("key=") && url.contains("qq")) {
                    wxService.updateRealUrl(url);
                    wxService.updateRealUrlV1(url);
                    if (url.contains("mp.weixin.qq.com/s")) {
                        wxService.updateRealUrlAndReadNum(data);
                    }
                }
            }

            String url = wxService.getNextRedirectUrl();
            String result;

            if (url == null) {
                result = JSONResult.newErrorResult(-1, "null task").toJson();
            } else {
                RequestUrlResponse requestUrlResponse = new RequestUrlResponse(url.replaceFirst("&key=[^&]*]", "").
                        replaceFirst("&pass_ticket=[^&]*]", "").
                        replaceFirst("&wechat_redirect", "").replaceFirst("#rd", "") + "#rd&wechat_redirect");
                result = jsonResult.setData(requestUrlResponse).toJson();
            }

            return String.format("%s(%s)", callback, result);
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("%s(%s)", callback, JSONResult.newErrorResult(-1, e.getMessage()).toJson());
        }
    }


    @RequestMapping(value = "wx/getVaildUrl")
    @ResponseBody
    public String getVaildUrl(HttpServletRequest request, @RequestParam("url") String url) {
        try {
            if (url != null) {
                return new JSONResult(wxService.getRedirectUrl(url)).toJson();
            } else {
                return JSONResult.newErrorResult(-1, "无效有URL地址").toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.newErrorResult(-1, e.getMessage()).toJson();
        }
    }

    @RequestMapping(value = "wx/add")
    @ResponseBody
    public String wxTaskAdd(@RequestParam("url") String url) {
        try {
            if (url != null) {
                wxService.addWxUrlToQueue(url);
                return new JSONResult<>().toJson();
            } else {
                return JSONResult.newErrorResult(-1, "无效有URL地址").toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.newErrorResult(-1, e.getMessage()).toJson();
        }
    }

    @RequestMapping(value = "wx/get")
    @ResponseBody
    public String resultGet(@RequestParam("key") String key) {
        try {
            if (key != null) {
                String result = wxService.getValueByKey(key);
                if(result == null) result = "";
                return new JSONResult<String>(result).toJson();
            } else {
                return JSONResult.newErrorResult(-1, "无效有URL地址").toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.newErrorResult(-1, e.getMessage()).toJson();
        }
    }

    @RequestMapping(value = "wx/update")
    @ResponseBody
    public String resultUpdate( @RequestParam("data") String data) {
        try {
            if (data !=null) {
                wxService.updateRealUrlAndReadNum(data);
                return new JSONResult<>().toJson();
            }else{
                return JSONResult.newErrorResult(-1, "无效的数据").toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.newErrorResult(-1, e.getMessage()).toJson();
        }
    }

    @RequestMapping(value = "wx/mapLinkAdd")
    @ResponseBody
    public String mapLinkAdd( @RequestParam("realUrl") String realUrl, @RequestParam("sogouUrl") String sogouUrl) {
        try {
            if (StringUtils.isNotBlank(realUrl) && StringUtils.isNotBlank(sogouUrl)) {
                wxService.mapLinkAdd(realUrl, sogouUrl);
                return new JSONResult<>().toJson();
            } else {
                return JSONResult.newErrorResult(-1, "无效的数据").toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.newErrorResult(-1, e.getMessage()).toJson();
        }
    }


    @RequestMapping(value = "wx/mapLinkGet")
    @ResponseBody
    public String mapLinkGet( @RequestParam("key") String key) {
        try {
            if (StringUtils.isNotBlank(key)) {
                return wxService.mapLinkGet(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
