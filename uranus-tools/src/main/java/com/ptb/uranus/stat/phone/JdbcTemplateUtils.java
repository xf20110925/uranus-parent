package com.ptb.uranus.stat.phone;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
public class JdbcTemplateUtils {

    private static JdbcTemplate jdbcTemplate;

    public static JdbcTemplate jdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate();
        }
        return jdbcTemplate;
    }

    private static JdbcTemplate createJdbcTemplate() {
        DruidDataSource ds = new DruidDataSource();
        Configuration conf = null;
        try {
            conf = new PropertiesConfiguration("uranus.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        ds.setDriverClassName(conf.getString("uranus.tool.phone.jdbc.driver"));
        ds.setUrl(conf.getString("uranus.tool.phone.jdbc.url"));
        ds.setUsername(conf.getString("uranus.tool.phone.jdbc.username"));
        ds.setPassword(conf.getString("uranus.tool.phone.jdbc.password"));

        return new JdbcTemplate(ds);
    }

}
