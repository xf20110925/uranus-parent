package com.ptb.uranus.asistant.web.controller;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.asistant.core.entity.JSONResult;
import com.ptb.uranus.asistant.web.model.rep.RequestUrlResponse;
import com.ptb.uranus.asistant.web.service.WxService;
import com.ptb.uranus.spider.weixin.WXSpider;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;

import kafka.utils.Json;
import org.apache.commons.collections.comparators.FixedOrderComparator;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by eric on 15/11/21.
 */
@Controller
public class WxController {
	static Logger logger = LoggerFactory.getLogger(WxController.class);


	@Resource
	WxService wxService;


	@ExceptionHandler({Exception.class})
	@ResponseBody
	public String AsisErrorHandler(Exception e) {
		return JSON.toJSONString(JSONResult.newErrorResult(e.getMessage()));
	}

	@RequestMapping(value = "wx/mobile/getNewUrl")
	@ResponseBody
	public String getNewUrl(HttpServletRequest request, @RequestParam("callback") String callback, @RequestParam("data") String data) {
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
				if (result == null) result = "";
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
	public String resultUpdate(@RequestParam("data") String data) {
		try {
			if (data != null) {
				wxService.updateRealUrlAndReadNum(data);
				return new JSONResult<>().toJson();
			} else {
				return JSONResult.newErrorResult(-1, "无效的数据").toJson();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResult.newErrorResult(-1, e.getMessage()).toJson();
		}
	}

	@RequestMapping(value = "wx/mapLinkAdd")
	@ResponseBody
	public String mapLinkAdd(@RequestParam("realUrl") String realUrl, @RequestParam("sogouUrl") String sogouUrl) {
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
	public String mapLinkGet(@RequestParam("key") String key) {
		try {
			if (StringUtils.isNotBlank(key)) {
				return wxService.mapLinkGet(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@RequestMapping(value = "wx/readLike", method = RequestMethod.POST)
	@ResponseBody
	public String getReadLikeByBaYou(@RequestBody ReadLikeRequest requestBody) {
		String url = requestBody.getUrl();
		if (StringUtils.isNotBlank(url)) {
			return wxService.getReadLikeByBaYou(url);
		}
		return null;
	}

	static class ReadLikeRequest {
		private String url;

		public ReadLikeRequest() {
		}

		public ReadLikeRequest(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "wx/readUrl")
	@ResponseBody
	public String getReadUrl(HttpServletRequest request, @RequestParam("callback") String callback, @RequestParam("url") String url) throws IOException {
		ReadLikeNum readLikeNum = WXSpider.getReadLikeNum(url);
		if (readLikeNum.getLikeNum() >= 0 && readLikeNum.getReadNum()>=0){
			String format = String.format("%sCountNum:%skeyUrl:%s", readLikeNum, counter.addAndGet(1), url);
			logger.info("success grab dynamic data:"+format);
			return String.format("%s(%s)", callback, readLikeNum);
		}else{
			logger.error("error grab dynamic data:"+url);
		}
		return String.format("%s(%s)", callback, null);
	}

}
