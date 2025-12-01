package com.mna.springbootsecurity.analytics.snowflake.config;

import com.mna.springbootsecurity.analytics.snowflake.service.SnowflakeService;
import com.mna.springbootsecurity.base.constant.Profiles;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Skipping this test temporarily")
@SpringBootTest
@Slf4j
public class SnowflakeIntegrationTest {

    @Autowired
    private Environment environment;

    @BeforeEach
    void setUp() {
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isSnowflakeProfileActive = Arrays.asList(activeProfiles).contains(Profiles.SNOWFLAKE);

        Assumptions.assumeTrue(isSnowflakeProfileActive, "'Snowflake' profile not active, skipping test.");
    }

    @Autowired(required = false)
    private SnowflakeService snowflakeService;

    @Test
    void testConnection() {
        String query = "SELECT 1 as One";
        List<Map<String, Object>> resultSet = snowflakeService.executeQuery(query);

        assertThat(resultSet).isNotEmpty();

        for (Map<String, Object> row : resultSet) {
            Object one = row.get("one");
            log.info("One : {}", one);

            assertThat(one).isEqualTo(1L);
            break;
        }

    }

    @Test
    void testQuery() {
        String query = "SELECT CURRENT_DATE AS CurrentDate";
        List<Map<String, Object>> results = snowflakeService.executeQuery(query);

        for (Map<String, Object> row : results) {
            Object currentDate = row.get("currentDate");
            log.info("Current Date: {}", currentDate);
        }

        assertThat(results).isNotEmpty();
    }
}
