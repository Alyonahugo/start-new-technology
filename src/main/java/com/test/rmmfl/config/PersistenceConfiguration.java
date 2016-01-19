package com.test.rmmfl.config;

import com.test.gdo.annotation.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfiguration {

    @Bean
    @Primary
    @ToolsDataSource
    @ConfigurationProperties(prefix = "spring.maindatasource")
    public DataSource toolsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ToolsTransactionManager
    public PlatformTransactionManager toolsTransactionManager(@ToolsDataSource DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @ToolsJdbcTemplate
    public JdbcTemplate toolsJdbcTemplate(@ToolsDataSource DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ReportingDataSource
    @ConfigurationProperties(prefix = "spring.redshiftdatasource")
    public DataSource reportingDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ReportingTransactionManager
    public PlatformTransactionManager reportingTransactionManager(@ReportingDataSource DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @ReportingJdbcTemplate
    public JdbcTemplate reportingJdbcTemplate(@ReportingDataSource DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
