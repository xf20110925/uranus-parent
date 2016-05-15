package com.ptb.uranus.asistant.web.service;

import com.ptb.uranus.asistant.core.proxy.ProxyAvaliableChecker;
import com.ptb.uranus.asistant.web.dao.ProxyDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 对代理进行管理,从一个地方接收代理的地址,并不断的对池中的代理做校验,识别其类型,可用性
 */
@Component
public class ProxyService {

    ProxyDao proxyDao;
    ProxyAvaliableChecker proxyAvaliableChecker;

    public ProxyService() {
        proxyDao = new ProxyDao();
        proxyAvaliableChecker = new ProxyAvaliableChecker(proxyDao);
        proxyAvaliableChecker.BeginCheck();
    }

    public String getProxy() {
        return proxyDao.getRandomProxy();
    }

    public void delProxy(String proxy) {
        proxyDao.del(proxy);
    }

    public void addProxy(List<String> proxys) {
        proxyDao.add(proxys);
    }

    public void addProxy(String proxy) {
        proxyDao.add(proxy);
    }
}
