package com.mna.springbootsecurity.datasource.config;

import com.mna.springbootsecurity.datasource.base.model.DataSourceProperties;
import com.mna.springbootsecurity.datasource.demo.DataSourcePropertiesMock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application.multi-datasource", name = "enabled", havingValue = "true")
@Slf4j
public class DynamicDataSourceConfig {

    private final DataSourceFactory factory;

    @Autowired(required = false) // Make it optional in case autoconfiguration fails
    private DataSource dataSource;

    //@Bean("dynamicDataSource")
    //@DependsOn("dataSource")
    public DataSource dynamicDataSource() {
        log.info("Initializing dynamicDataSource with multi-tenancy support");

        List<DataSourceProperties> dataSourceProperties = DataSourcePropertiesMock.get();
        log.debug("Loaded {} data source configurations from mock data", dataSourceProperties.size());

        Map<Object, Object> dataSources = dataSourceProperties.stream()
                .peek(ds -> log.debug("Creating DataSource for key: {}", ds.getKey()))
                .collect(Collectors.toMap(DataSourceProperties::getKey, factory::create));

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(dataSources);

        // Add the primary DataSource if it exists
        if (dataSource != null) {
            dataSources.put("primary", dataSource);
            log.info("Added auto-configured primary DataSource to routing.");
            routingDataSource.setDefaultTargetDataSource(dataSource);
        } else {
            String defaultDataSourceKey = dataSourceProperties.get(0).getKey();
            routingDataSource.setDefaultTargetDataSource(factory.get(defaultDataSourceKey));
            log.warn("Primary DataSource not found. Using {} as default.", defaultDataSourceKey);
        }

        log.info("RoutingDataSource initialized with {} data sources.", dataSources.size());
        return routingDataSource;
    }

   // @Bean("dynamicJdbcTemplate")
   // @DependsOn("dynamicDataSource")
    public JdbcTemplate dynamicJdbcTemplate(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        log.info("Initializing dynamicJdbcTemplate with dynamicDataSource");
        return new JdbcTemplate(dynamicDataSource);
    }
}