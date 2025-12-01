package com.mna.springbootsecurity.domain.dao;

import com.mna.springbootsecurity.domain.entity.DataSourceProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSourcePropertiesDao extends JpaRepository<DataSourceProperties, Integer> {
}
