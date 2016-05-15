package com.ptb.uranus.asistant.core.proxy;

import com.ptb.uranus.asistant.web.dao.ProxyDao;

import java.util.Set;

/**
 * Created by eric on 16/1/26.
 */
public class ProxyAvaliableChecker {
    private final ProxyDao proxyDao;
    int checkInterval = 30000;

    public ProxyAvaliableChecker(ProxyDao proxyDao) {
        this.proxyDao = proxyDao;
    }

    public void BeginCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Set<String> proxys = proxyDao.getAllProxy();
                        //删除无效代理量
                        proxys.parallelStream().forEach((proxy -> {
                            if (!this.isGoodProxy(proxy)) {
                                proxyDao.del(proxy);
                            }
                        }));

                        //休息一段时间
                        try {
                            Thread.sleep(checkInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {

                    }

                }
            }

            private boolean isGoodProxy(String proxy) {
                return ProxyBuilder.checkProxyIsGood(proxy);
            }
        }).start();
    }
}
