package com.mna.springbootsecurity.base.constant;

public class Profiles {

    private Profiles() {
    }

    public static final String REDIS = "redis";
    public static final String SNOWFLAKE = "snowflake";

    public static final String[] mandatory = { REDIS };


}
