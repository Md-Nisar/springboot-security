package com.mna.springbootsecurity.scheduling.util;


import org.springframework.scheduling.support.CronExpression;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class CronUtil {

    public static String generateDailyCron(int hour, int minute) {
        return String.format("0 %d %d * * ?", minute, hour);
    }

    public static String generateWeeklyCron(DayOfWeek dayOfWeek, int hour, int minute) {
        return String.format("0 %d %d ? * %d", minute, hour, dayOfWeek.getValue());
    }

    public static String generateMonthlyCron(int dayOfMonth, int hour, int minute) {
        return String.format("0 %d %d %d * ?", minute, hour, dayOfMonth);
    }

    public static String generateEveryXMinutesCron(int intervalMinutes) {
        return String.format("0 0/%d * * * ?", intervalMinutes);
    }

    public static String generateEveryXHoursCron(int intervalHours) {
        return String.format("0 0 0/%d * * ?", intervalHours);
    }

    public static boolean isValidCronExpression(String cronExpression) {
        try {
            CronExpression.parse(cronExpression);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Optional<ZonedDateTime> getNextExecution(String cronExpression, ZoneId zoneId) {
        if (!isValidCronExpression(cronExpression)) {
            return Optional.empty();
        }
        CronExpression cron = CronExpression.parse(cronExpression);
        LocalDateTime now = LocalDateTime.now();
        return Optional.ofNullable(cron.next(ZonedDateTime.of(now, zoneId)));
    }
}
