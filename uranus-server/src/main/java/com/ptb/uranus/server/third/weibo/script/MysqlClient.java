package com.ptb.uranus.server.third.weibo.script;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.server.third.weibo.entity.FreshData;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by watson zhang on 16/6/15.
 */
public enum MysqlClient {
	instance;

	private String driverClassName = "com.mysql.jdbc.Driver";
	private String mysqlHost;
	private String mysqlUser;
	private String mysqlPwd;
	JdbcTemplate template;

	MysqlClient() {
		initConn();
	}

	private void initConn() {
		try {
			PropertiesConfiguration conf = new PropertiesConfiguration("uranus.properties");
			mysqlHost = conf.getString("uranus.bayou.mysqlHost", String.format("jdbc:mysql://%s?useUnicode=true&characterEncoding=utf-8", "43.241.214.85:3306/weibo"));
			mysqlUser = conf.getString("uranus.bayou.mysqlUser", "pintuibao");
			mysqlPwd = conf.getString("uranus.bayou.mysqlPwd", "pintuibao");
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(mysqlHost);
			dataSource.setUsername(mysqlUser);
			dataSource.setPassword(mysqlPwd);
			template = new JdbcTemplate(dataSource);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public <T> List<T> query(String sql, Class<T> clazz, Long... args){
		List<T> ret = template.queryForList(sql, args).parallelStream().map(obj -> JSON.parseObject(JSON.toJSONString(obj), clazz)).collect(Collectors.toList());
		return ret;
	}

	public static void main(String[] args) {
		String sql = "select * from fresh_data where id > ?  LIMIT ?";
		List<FreshData> query = MysqlClient.instance.query(sql, FreshData.class, Long.valueOf(0), Long.valueOf(10));
		query.forEach(x -> System.out.println(JSON.toJSONString(x)));
	}

}

