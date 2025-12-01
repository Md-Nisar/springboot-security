package com.mna.springbootsecurity.datasource.demo;

import com.mna.springbootsecurity.datasource.base.enums.DatabaseType;
import com.mna.springbootsecurity.datasource.base.model.DataSourceProperties;

import java.util.List;

public class DataSourcePropertiesMock {

    public static List<DataSourceProperties> get() {
        DataSourceProperties school = DataSourceProperties.builder()
                .key("ds-mysql-school").dbType(DatabaseType.MYSQL)
                .host("localhost").port(3306)
                .username("root").password("root")
                .databaseName("sbs_school")
                .build();

        DataSourceProperties hospital = DataSourceProperties.builder()
                .key("ds-mysql-hospital").dbType(DatabaseType.MYSQL)
                .host("localhost").port(3306)
                .username("root").password("root")
                .databaseName("sbs_hospital")
                .build();

        return List.of(school, hospital);
    }
}
