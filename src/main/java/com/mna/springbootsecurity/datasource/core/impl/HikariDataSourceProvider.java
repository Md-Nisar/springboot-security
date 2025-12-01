package com.mna.springbootsecurity.datasource.core.impl;

import com.mna.springbootsecurity.datasource.base.enums.DataSourceProviderType;
import com.mna.springbootsecurity.datasource.base.model.DataSourceProperties;
import com.mna.springbootsecurity.datasource.core.DataSourceProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@ConditionalOnProperty(prefix = "application.multi-datasource", name = "enabled", havingValue = "true")
@Slf4j
public class HikariDataSourceProvider implements DataSourceProvider {

    @Override
    public DataSource createDataSource(DataSourceProperties properties) {
        log.info("Creating Hikari DataSource for key: {}", properties.getKey());
        log.debug("Properties - URL: {}, Username: {}, Driver: {}",
                properties.getJDBCUrl(), properties.getUsername(), properties.getDriverClassName());

        HikariConfig hikari = new HikariConfig();

        hikari.setJdbcUrl(properties.getJDBCUrl());
        hikari.setUsername(properties.getUsername());
        hikari.setPassword(properties.getPassword());
        hikari.setDriverClassName(properties.getDriverClassName());

        hikari.setMaximumPoolSize(10);              // Max number of connections
        hikari.setMinimumIdle(1);                   // Min idle connections
        hikari.setIdleTimeout(2 * 60 * 1000);       // 2 minutes
        hikari.setMaxLifetime(1800000);             // 30 minutes
        hikari.setConnectionTimeout(30000);         // 30 seconds to wait for connection
        hikari.setValidationTimeout(5000);          // 5 seconds for validation
        hikari.setLeakDetectionThreshold(60000);    // 60s to detect connection leaks
        hikari.setPoolName(properties.getKey());

        hikari.setConnectionTestQuery("SELECT 1");
        hikari.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

        hikari.setAutoCommit(true);

        log.info("Hikari DataSource [{}] configured successfully", properties.getKey());
        return new HikariDataSource(hikari);
    }

    @Override
    public DataSourceProviderType getType() {
        return DataSourceProviderType.HIKARI;
    }
}
