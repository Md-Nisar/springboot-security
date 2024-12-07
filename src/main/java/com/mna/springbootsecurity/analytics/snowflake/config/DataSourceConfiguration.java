package com.mna.springbootsecurity.analytics.snowflake.config;

import com.mna.springbootsecurity.analytics.snowflake.helper.UrlBuilderHelper;
import com.mna.springbootsecurity.base.constant.Profiles;
import com.mna.springbootsecurity.base.property.SnowflakeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Profile(Profiles.SNOWFLAKE)
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSourceConfiguration {

    private BasicDataSource snowflakeDataSource;
    private final SnowflakeProperties snowflakeProperties;

    public final long MAX_WAIT_MILLIS = 10000L; // 10 seconds
    public final long TIME_BETWEEN_EVICTION_RUNS_MILLIS = 300000L; // 5 minutes
    public final long MIN_EVICTABLE_IDLE_TIME_MILLIS = 600000L; // 10 minutes
    public final int NUM_TESTS_PER_EVICTION_RUN = 3;
    public final int REMOVE_ABANDONED_TIMEOUT = 300; // 5 minutes

    @Bean(name = "snowflakeJdbcTemplate")
    public JdbcTemplate snowflakeJdbcTemplate() {
        snowflakeDataSource = new BasicDataSource();

        try {
            String url = UrlBuilderHelper.buildSnowflakeUrl(
                    snowflakeProperties.getUrl(),
                    snowflakeProperties.getDatabase(),
                    snowflakeProperties.getSchema(),
                    snowflakeProperties.getWarehouse()
            );

            log.info("Connecting to Snowflake with URL: {}", url);

            snowflakeDataSource.setUrl(url);
            snowflakeDataSource.setUsername(snowflakeProperties.getUsername());
            snowflakeDataSource.setPassword(snowflakeProperties.getPassword());

            snowflakeDataSource.setInitialSize(5);
            snowflakeDataSource.setMaxTotal(20);
            snowflakeDataSource.setMinIdle(5);
            snowflakeDataSource.setMaxIdle(10);

            snowflakeDataSource.setTestOnBorrow(true);
            snowflakeDataSource.setTestOnReturn(false);
            snowflakeDataSource.setTestWhileIdle(true);
            snowflakeDataSource.setValidationQuery("SELECT 1");

            snowflakeDataSource.setMaxWaitMillis(MAX_WAIT_MILLIS);
            snowflakeDataSource.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUNS_MILLIS);
            snowflakeDataSource.setMinEvictableIdleTimeMillis(MIN_EVICTABLE_IDLE_TIME_MILLIS);
            snowflakeDataSource.setNumTestsPerEvictionRun(NUM_TESTS_PER_EVICTION_RUN);

            snowflakeDataSource.setRemoveAbandonedOnMaintenance(true);
            snowflakeDataSource.setRemoveAbandonedTimeout(REMOVE_ABANDONED_TIMEOUT);
            snowflakeDataSource.setLogAbandoned(true);

            log.info("Snowflake DataSource successfully configured.");
            return new JdbcTemplate(snowflakeDataSource);

        } catch (Exception e) {
            log.error("Error configuring Snowflake DataSource: ", e);
            throw new RuntimeException("Error configuring Snowflake DataSource", e);
        }
    }

    public void logDataSourceDetails() {
        if (snowflakeDataSource != null) {
            log.info("Active Connections: {} Idle Connections: {}", snowflakeDataSource.getNumActive(), snowflakeDataSource.getNumIdle());
        } else {
            log.warn("Snowflake DataSource is not initialized.");
        }
    }

}
