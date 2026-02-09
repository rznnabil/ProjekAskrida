package com.askrida.web.service.conf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Repository;

@Configuration
public class MasterBussiness {

	public JdbcTemplate jdbcTemplate;
	protected Logger log = Logger.getLogger(this.getClass());

	@Bean(name = "db1")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource1() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate1")
	public JdbcTemplate jdbcTemplate1(@Qualifier("db1") DataSource ds) throws SQLException {
		Connection connection = ds.getConnection();
		connection.setAutoCommit(false);
		jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
		return jdbcTemplate;
	}

	@Bean(name = "db2")
	@ConfigurationProperties(prefix = "spring.second-db")
	public DataSource dataSource2() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate1")
	public JdbcTemplate jdbcTemplate2(@Qualifier("db2") DataSource ds) throws SQLException {
		Connection connection = ds.getConnection();
		connection.setAutoCommit(false);
		return new JdbcTemplate(new SingleConnectionDataSource(connection, true));
	}

}
