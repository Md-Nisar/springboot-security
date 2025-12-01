package com.mna.springbootsecurity.datasource.core;

import com.mna.springbootsecurity.datasource.base.enums.DataSourceProviderType;
import com.mna.springbootsecurity.datasource.base.model.DataSourceProperties;

import javax.sql.DataSource;

public interface DataSourceProvider {

    DataSource createDataSource(DataSourceProperties properties);

    DataSourceProviderType getType();
}
