package com.mna.springbootsecurity.domain.entity;


import com.mna.springbootsecurity.datasource.base.enums.DataSourceProviderType;
import com.mna.springbootsecurity.datasource.base.enums.DatabaseType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CORE_DATA_SOURCE_PROPS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DataSourceProperties {

    @Id
    @Column(name = "DSC_PK_ID", nullable = false, unique = true)
    private Integer id;

    @Column(name = "HOST", nullable = false)
    private String host;

    @Column(name = "PORT", nullable = false)
    private int port;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "DATABASE_NAME", nullable = false)
    private String databaseName;

    @Enumerated(EnumType.STRING)
    @Column(name = "DATABASE_TYPE", nullable = false)
    private DatabaseType dbType;

    @Enumerated(EnumType.STRING)
    @Column(name = "DATA_SOURCE_PROVIDER")
    private DataSourceProviderType dataSourceProvider = DataSourceProviderType.HIKARI;
}

