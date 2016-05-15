package com.ptb.uranus.asistant.web.dao;

import com.ptb.uranus.asistant.core.util.RedisUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eric on 16/1/26.
 */
public class ProxyDao {
    static Logger logger = Logger.getLogger(ProxyDao.class);
    static String ProxySetKey = "proxy";

    public String getRandomProxy() {
        Jedis jedis = RedisUtil.getJedis();
        try {
            return jedis.srandmember(ProxySetKey);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                RedisUtil.returnResource(jedis);
            }
        }
        return null;

    }

    public Set<String> getAllProxy() {
        Jedis jedis = RedisUtil.getJedis();
        try {
            return jedis.smembers(ProxySetKey);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                RedisUtil.returnResource(jedis);
            }
        }
        return new HashSet<>();
    }

    public void add(List<String> newProxys) {
        Jedis jedis = RedisUtil.getJedis();
        try {
            newProxys.forEach((proxy) -> {
                jedis.sadd(ProxySetKey, proxy);
            });

        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                RedisUtil.returnResource(jedis);
            }
        }
    }

    public void add(String newProxy) {
        Jedis jedis = RedisUtil.getJedis();
        try {
            jedis.sadd(ProxySetKey, newProxy);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                RedisUtil.returnResource(jedis);
            }
        }
    }

    public void del(String proxy) {
        Jedis jedis = RedisUtil.getJedis();
        try {
            jedis.srem(ProxySetKey, proxy);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                RedisUtil.returnResource(jedis);
            }
        }
    }
}
