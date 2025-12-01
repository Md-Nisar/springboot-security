package com.mna.springbootsecurity.datasource.config;

import com.mna.springbootsecurity.datasource.base.enums.DataSourceProviderType;
import com.mna.springbootsecurity.datasource.base.model.DataSourceProperties;
import com.mna.springbootsecurity.datasource.core.DataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(prefix = "application.multi-datasource", name = "enabled", havingValue = "true")
@Slf4j
public class DataSourceFactory {

    private final Map<String, DataSource> inMemory = new ConcurrentHashMap<>();
    private final Map<DataSourceProviderType, DataSourceProvider> providers;

    @Autowired
    public DataSourceFactory(List<DataSourceProvider> providerList) {
        this.providers = providerList.stream()
                .collect(Collectors.toMap(DataSourceProvider::getType, p -> p));
        log.info("Initialized DataSourceProviders: {}", providers.keySet());
    }

    public DataSource create(DataSourceProperties properties) {
        String key = properties.getKey();

        if (inMemory.containsKey(key)) {
            log.info("Returning existing DataSource for key: {}", key);
            return inMemory.get(key);
        }

        DataSourceProviderType type = properties.getDataSourceProvider();
        log.info("Creating new DataSource for key: {}, provider type: {}", key, type);

        DataSourceProvider provider = providers.getOrDefault(type, providers.get(DataSourceProviderType.HIKARI));
        if (provider == null) {
            log.error("No DataSourceProvider found for type: {}", type);
            throw new IllegalStateException("No DataSourceProvider found for type: " + type);
        }

        DataSource ds = provider.createDataSource(properties);
        inMemory.put(key, ds);

        log.info("DataSource [{}] created and stored in memory", key);
        return ds;
    }

    public DataSource get(String key) {
        log.debug("Fetching DataSource for key: {}", key);
        DataSource ds = inMemory.get(key);

        if (ds == null) {
            log.warn("No DataSource found in memory for key: {}", key);
        } else {
            log.debug("DataSource found for key: {}", key);
        }

        return ds;
    }

    public void remove(String key) {
        log.debug("Removing DataSource for key: {}", key);
        if (inMemory.containsKey(key)) {
            try {
                inMemory.remove(key);
                log.info("DataSource successfully removed from the memory for key: {}", key);
            } catch (Exception e) {
                log.error("Failed to DataSource from memory", e);
            }
        }
    }

}
