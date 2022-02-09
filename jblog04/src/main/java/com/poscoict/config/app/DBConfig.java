package com.poscoict.config.app;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:com/poscoict/jblog/config/app/jdbc.properties")
public class DBConfig {

	@Autowired
	private Environment env;
	
	// Connection Pool DataSource
	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		
		// 기본 값
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		// xml이 아니기에 &amp; -> &
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		
		// 추가 설정, 커넥션 풀과 관련된 사항들...
		dataSource.setInitialSize(env.getProperty("jdbc.initialSize", Integer.class));
		dataSource.setMaxActive(env.getProperty("jdbc.maxActive", Integer.class));
		
		return dataSource;
	}
	
	
	
	
	
}
