package com.doctor_service.config;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.doctor_service.command.repositories",
        entityManagerFactoryRef = "commandEntityManagerFactory",
        transactionManagerRef = "commandTransactionManager"
)
public class CommandDataSourceConfig {



    // Command DB (write)
    @Primary
    @Bean(name = "commandDataSource")
    public DataSource commandDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/doctor_write_service");
        dataSource.setUsername("root");
        dataSource.setPassword("Ashutosh@2000");
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
                .packages("com.doctor_service.command.entities")
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
