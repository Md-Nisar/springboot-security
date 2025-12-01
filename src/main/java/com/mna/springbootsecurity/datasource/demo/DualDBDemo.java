package com.mna.springbootsecurity.datasource.demo;

import com.mna.springbootsecurity.datasource.annotation.UseDataSource;
import com.mna.springbootsecurity.datasource.context.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(prefix = "application.multi-datasource", name = "enabled", havingValue = "true")
@Slf4j
public class DualDBDemo {

    // private final JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    @Qualifier("dynamicJdbcTemplate")
    private JdbcTemplate dynamicJdbcTemplate;


    public void useHospitalDB() {
        // Default DB
        //  List<Map<String, Object>> defaultResult = defaultJdbc.queryForList("SHOW TABLES FROM springboot_security");

        try {
            DataSourceContextHolder.set("ds-mysql-hospital");

            String sql = "SHOW TABLES FROM sbs_hospital";
            List<Map<String, Object>> resultSet = dynamicJdbcTemplate.queryForList(sql);

            log.info("Query: {}", sql);
            log.info("ResultSet: {}", resultSet);
        } finally {
            DataSourceContextHolder.clear();
        }

    }

    @UseDataSource("ds-mysql-school")
    public void useSchoolDB() {
        String sql = "SHOW TABLES FROM sbs_school";
        var result = dynamicJdbcTemplate.queryForList(sql);
        result.forEach(System.out::println);
    }

}