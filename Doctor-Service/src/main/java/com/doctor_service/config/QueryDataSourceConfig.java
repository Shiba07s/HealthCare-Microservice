package com.doctor_service.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.doctor_service.query.repositories",
        entityManagerFactoryRef = "queryEntityManagerFactory",
        transactionManagerRef = "queryTransactionManager"
)
public class QueryDataSourceConfig {

    // Query DB (read)
    @Bean(name = "queryDataSource")
    public DataSource queryDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/doctor_read_service");
        dataSource.setUsername("root");
        dataSource.setPassword("Ashutosh@2000");
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
                .packages("com.doctor_service.query.entities")
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