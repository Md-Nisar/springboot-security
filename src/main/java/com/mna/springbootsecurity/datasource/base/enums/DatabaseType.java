package com.mna.springbootsecurity.datasource.base.enums;

import lombok.Getter;

@Getter
public enum DatabaseType {

    MYSQL("jdbc:mysql://%s:%d/%s", "com.mysql.cj.jdbc.Driver"),
    POSTGRESQL("jdbc:postgresql://%s:%d/%s", "org.postgresql.Driver"),
    ORACLE("jdbc:oracle:thin:@%s:%d:%s", "oracle.jdbc.OracleDriver"),
    SQLSERVER("jdbc:sqlserver://%s:%d;databaseName=%s", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    H2("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1", "org.h2.Driver"),
    MARIADB("jdbc:mariadb://%s:%d/%s", "org.mariadb.jdbc.Driver"),
    SQLITE("jdbc:sqlite:%s", "org.sqlite.JDBC");

    private final String urlTemplate;
    private final String driverClassName;

    DatabaseType(String urlTemplate, String driverClassName) {
        this.urlTemplate = urlTemplate;
        this.driverClassName = driverClassName;
    }

    public String buildJDBCUrl(String host, int port, String dbName) {
        if (this == H2) return String.format(urlTemplate, dbName);

        return String.format(urlTemplate, host, port, dbName);
    }

}

