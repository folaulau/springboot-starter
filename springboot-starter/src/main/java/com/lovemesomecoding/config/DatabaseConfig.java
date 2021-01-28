package com.lovemesomecoding.config;

import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author folaukaveinga
 */
@Slf4j
@Profile({"local", "dev", "prod"})
@Configuration
// @EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseConfig {

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.database}")
    private String dbName;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    /* ================== datasource =============== */
    @Bean
    public HikariDataSource dataSource() {
        log.info("Configuring dataSource...");

        log.info("dbUrl={}", dbUrl);
        log.info("dbUsername={}", dbUsername);
        log.info("dbPassword={}", dbPassword);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource hds = new HikariDataSource(config);
        hds.setMaximumPoolSize(30);
        hds.setMinimumIdle(5);
        hds.setMaxLifetime(1800000);
        hds.setConnectionTimeout(30000);
        hds.setIdleTimeout(600000);
        // 45 seconds
        hds.setLeakDetectionThreshold(45000);

        log.info("DataSource configured!");

        return hds;
    }

    // @Bean
    // public AuditorAware<String> auditorAware() {
    // return new AuditorAware<String>() {
    //
    // @Override
    // public Optional<String> getCurrentAuditor() {
    // String user = "SYSTEM";
    //
    // return Optional.ofNullable(user);
    //
    // }
    //
    // };
    // }

    // /**
    // * Override default flyway initializer to do nothing
    // */
    // @Bean
    // FlywayMigrationInitializer flywayInitializer() {
    // return new FlywayMigrationInitializer(setUpFlyway(), (f) -> {// do nothing
    // });
    // }
    //
    // /**
    // * Create a second flyway initializer to run after jpa has created the schema
    // */
    // @Bean
    // @DependsOn("dataSource")
    // FlywayMigrationInitializer delayedFlywayInitializer() {
    // Flyway flyway = setUpFlyway();
    // return new FlywayMigrationInitializer(flyway, null);
    // }
    //
    // private Flyway setUpFlyway() {
    //
    // FluentConfiguration configuration = Flyway.configure().dataSource(dbApiUrl, dbUsername, dbPassword);
    // configuration.schemas(dbName);
    // configuration.baselineOnMigrate(true);
    // return configuration.load();
    // }

}
