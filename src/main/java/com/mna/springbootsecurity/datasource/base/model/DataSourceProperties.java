package com.mna.springbootsecurity.datasource.base.model;

import com.mna.springbootsecurity.datasource.base.enums.DataSourceProviderType;
import com.mna.springbootsecurity.datasource.base.enums.DatabaseType;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DataSourceProperties {
    private String key;
    private String host;
    private int port;
    private String username;
    private String password;
    private String databaseName;
    private DatabaseType dbType;
    private DataSourceProviderType dataSourceProvider = DataSourceProviderType.HIKARI; //default

    public String getJDBCUrl() {
        return dbType.buildJDBCUrl(host, port, databaseName);
    }

    public String getDriverClassName() {
        return dbType.getDriverClassName();
    }


}
