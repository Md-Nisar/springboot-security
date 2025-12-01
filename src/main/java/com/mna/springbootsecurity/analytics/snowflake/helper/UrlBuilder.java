package com.mna.springbootsecurity.analytics.snowflake.helper;

public class UrlBuilder {

    public static String buildSnowflakeUrl(String baseUrl, String database, String schema, String warehouse) {
        return baseUrl + "/?db=" + database + "&schema=" + schema + "&warehouse=" + warehouse;
    }
}

