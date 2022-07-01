package com.example.examplespring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// 데이터베이스 연결 설정
@Configuration
public class DatabaseConfiguration {

    // DataSource @Bean 등록
    @Bean
    // 프로퍼티 자동 set (application.properties 속 spring.datasource)
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

}