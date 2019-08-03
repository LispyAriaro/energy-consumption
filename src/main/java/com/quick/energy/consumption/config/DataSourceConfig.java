package com.quick.energy.consumption.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.quick.energy.consumption"},
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUserName;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @Value("${spring.datasource.driver-class}")
    private String jdbcDriver;

    @Value("${spring.datasource.max-active}")
    private String maxActive;

    @Value("${liquibase.changelog}")
    String liquidbaseChangeLogPath;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("VillagePower_Consumption_HikariPool");
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(jdbcUserName);
        dataSource.setPassword(jdbcPassword);
        dataSource.setMaximumPoolSize(Integer.parseInt(maxActive));

        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        Map<String, String> prop = new HashMap<>();

        prop.put("hibernate.ejb.naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaPropertyMap(prop);
        factoryBean.setPackagesToScan("com.quick.energy.consumption");

        return factoryBean;
    }

    @Bean
    @Primary
    @DependsOn(value = "entityManagerFactory")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    @Primary
    public SpringLiquibase liquibase(@Qualifier("dataSource") DataSource dSource) {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dSource);
        liquibase.setChangeLog(liquidbaseChangeLogPath);
        liquibase.setShouldRun(true);

        return liquibase;
    }
}
