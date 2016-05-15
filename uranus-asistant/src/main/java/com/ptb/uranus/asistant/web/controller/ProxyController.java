package com.ptb.uranus.asistant.web.controller;

import com.ptb.uranus.asistant.web.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by eric on 16/1/26.
 */
@Controller
public class ProxyController {

    @Autowired
    ProxyService proxyService;

    @RequestMapping("proxy/get")
    @ResponseBody
    public String getVaildProxy() {
        String proxy = proxyService.getProxy();
        return proxy;
    }

    @RequestMapping("proxy/add")
    @ResponseBody
    public String addProxy(@RequestParam("proxy")  Object proxy) {
        if (proxy instanceof List){
            proxyService.addProxy((List<String>) proxy);
        }else{
            proxyService.addProxy((String)proxy);
        }
        return proxy.toString();
    }

    @RequestMapping("proxy/del")
    @ResponseBody
    public String delProxy(@RequestParam("proxy") String proxy) {
        proxyService.delProxy(proxy);
        return proxy;
    }
    public void setProxyService(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    public ProxyService getProxyService() {
        return proxyService;
    }
}
