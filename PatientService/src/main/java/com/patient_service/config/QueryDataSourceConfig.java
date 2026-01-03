package com.patient_service.config;

import com.patient_service.query.dtos.QueryDatabasePayload;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(
        basePackages = "com.patient_service.query.repository",
        entityManagerFactoryRef = "queryEntityManagerFactory",
        transactionManagerRef = "queryTransactionManager"
)
public class QueryDataSourceConfig {

    private final QueryDatabasePayload databasePayload;
    // Query DB (read)
    @Bean(name = "queryDataSource")
    public DataSource queryDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(databasePayload.getUrl());
        dataSource.setUsername(databasePayload.getUsername());
        dataSource.setPassword(databasePayload.getPassword());
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean(name = "queryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean queryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("queryDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", true);

        return builder
                .dataSource(dataSource)
                .packages("com.patient_service.query.entities")
                .persistenceUnit("query")
                .properties(properties)
                .build();
    }

    @Bean(name = "queryTransactionManager")
    public PlatformTransactionManager queryTransactionManager(
            @Qualifier("queryEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}