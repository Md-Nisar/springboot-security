package com.mna.springbootsecurity.analytics.snowflake.service;

import com.mna.springbootsecurity.base.constant.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Profile(Profiles.SNOWFLAKE)
@Service
public class SnowflakeService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SnowflakeService(@Qualifier("snowflakeJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQuery(String query) {
        return jdbcTemplate.queryForList(query);
    }

}
