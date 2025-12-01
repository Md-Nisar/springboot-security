package com.mna.springbootsecurity.scheduling.base.constant;

/**
 * CronExpression stores all the cron expressions used in the application.
 * This ensures consistency and makes it easier to manage schedules.
 *
 * @author [Md Nisar Ahmed]
 * @version 1.0
 */
public class CronExpression {

    /**
     Cron expression for cleaning up blacklisted JWT tokens every 30 minutes
     */
    public static final String JWT_BLACKLISTED_TOKEN_CLEANUP = "0 */30 * * * *";

}

