package com.mna.springbootsecurity.analytics.snowflake.helper;

public class UrlBuilderHelper {

    public static String buildSnowflakeUrl(String baseUrl, String database, String schema, String warehouse) {
        return baseUrl + "/?db=" + database + "&schema=" + schema + "&warehouse=" + warehouse;
    }
}

