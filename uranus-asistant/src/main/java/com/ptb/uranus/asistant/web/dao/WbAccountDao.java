package com.ptb.uranus.asistant.web.dao;


import com.alibaba.fastjson.JSON;
import com.ptb.uranus.asistant.core.util.RedisUtil;
import com.ptb.uranus.asistant.web.model.RWbAcctMeta;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * Created by eric on 16/8/29.
 */
@Component
public class WbAccountDao {
	static final String WB_ACCOUT_SET_KEY = "WB:ACCOUNT:SET";

	public void addWeiboCount(String username, String password) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.hsetnx(WB_ACCOUT_SET_KEY, username, JSON.toJSONString(new RWbAcctMeta(username, password)));
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public void delWeiboCount(String username) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.hdel(WB_ACCOUT_SET_KEY,username);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public List<RWbAcctMeta> getAllWbAccount() {
		Jedis jedis = RedisUtil.getJedis();
		List<RWbAcctMeta> rWbAcctMetas = new LinkedList<>();
		try {

			Map<String, String> accoutKV = jedis.hgetAll(WB_ACCOUT_SET_KEY);
			if(accoutKV != null && accoutKV.size() > 0) {
				for (String s : accoutKV.values()) {
					rWbAcctMetas.add(JSON.parseObject(s,RWbAcctMeta.class));
				}
			}
		} finally {
			RedisUtil.returnResource(jedis);
		}
		return rWbAcctMetas;
	}

	public void addWeiboCount(RWbAcctMeta rWbAcctMeta) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.hsetnx(WB_ACCOUT_SET_KEY, rWbAcctMeta.getUsername(), JSON.toJSONString(rWbAcctMeta));
		} finally {
			RedisUtil.returnResource(jedis);
		}

	}
}
