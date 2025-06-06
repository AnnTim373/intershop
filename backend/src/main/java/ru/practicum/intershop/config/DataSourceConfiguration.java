package ru.practicum.intershop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Value("${spring.liquibase.url}")
    private String url;
    @Value("${spring.liquibase.user}")
    private String username;
    @Value("${spring.liquibase.password}")
    private String password;
    @Value("${spring.liquibase.driver-class-name}")
    private String driver;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

}
