package com.mna.springbootsecurity.datasource.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Slf4j
public class DataSourceMonitorUtil {

    private DataSourceMonitorUtil() {
    }

    public static void printConnectionDetails(Connection connection, String label) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();

            log.info("===== [{}] Database Connection Info =====", label);
            log.info("JDBC URL         : {}", metaData.getURL());
            log.info("Username         : {}", metaData.getUserName());
            log.info("Driver Name      : {}", metaData.getDriverName());
            log.info("Driver Version   : {}", metaData.getDriverVersion());
            log.info("Database Name    : {}", metaData.getDatabaseProductName());
            log.info("Database Version : {}", metaData.getDatabaseProductVersion());
            log.info("Auto Commit      : {}", connection.getAutoCommit());
            log.info("Read Only        : {}", connection.isReadOnly());
            log.info("==========================================");

        } catch (SQLException e) {
            log.error("Failed to retrieve connection metadata: {}", e.getMessage(), e);
        }
    }
}

