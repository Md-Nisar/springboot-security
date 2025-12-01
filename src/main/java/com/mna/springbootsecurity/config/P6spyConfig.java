package com.mna.springbootsecurity.config;

import com.p6spy.engine.spy.appender.SingleLineFormat;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Configuration
public class P6spyConfig extends SingleLineFormat {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ROOT);

    public String getNow() {
        return DATE_FORMAT.format(new Date());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return String.format(
                Locale.ROOT,
                "%s [ %3dms ]  %-10s : %s",
                getNow(), elapsed, category, sql.replaceAll("\n", " ")
        );
    }
}
