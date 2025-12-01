package com.mna.springbootsecurity.base.constant;

public class Profiles {

    private Profiles() {
    }

    public static final String DEV = "dev";
    public static final String PROD = "prod";

    public static final String REDIS = "redis";
    public static final String RABBITMQ = "rabbitmq";
    public static final String SNOWFLAKE = "snowflake";
    public static final String DOCKER = "docker";


    public static final String[] mandatory = {REDIS};


}
