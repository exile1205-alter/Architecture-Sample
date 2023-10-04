package com.example.demo;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfiguration {

    /**
     * Auto-configured DataSource
     */
    @Bean(name="dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") HikariDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}