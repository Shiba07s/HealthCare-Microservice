package com.patient_service.config;


import com.patient_service.command.dtos.CommandDatabasePayload;
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
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.patient_service.command.repository",
        entityManagerFactoryRef = "commandEntityManagerFactory",
        transactionManagerRef = "commandTransactionManager"
)
public class CommandDataSourceConfig {

    private final CommandDatabasePayload databasePayload;


    // Command DB (write)
    @Primary
    @Bean(name = "commandDataSource")
    public DataSource commandDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(databasePayload.getUrl());
        dataSource.setUsername(databasePayload.getUsername());
        dataSource.setPassword(databasePayload.getPassword());
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Primary
    @Bean(name = "commandEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commandEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("commandDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", true);

        return builder
                .dataSource(dataSource)
                .packages("com.patient_service.command.entities")
                .persistenceUnit("command")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "commandTransactionManager")
    public PlatformTransactionManager commandTransactionManager(
            @Qualifier("commandEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
