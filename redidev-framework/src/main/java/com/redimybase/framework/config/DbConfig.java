package com.redimybase.framework.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/20 23:49
 */
@Configuration
public class DbConfig {

    @Value("${spring.application.name:'all'}")
    private String appName;

    @Bean( initMethod = "migrate", name = "flyway")
    public Flyway getMigration(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .validateOnMigrate(Boolean.FALSE)
//                .locations("db.migration")
                .table(String.format("schema_version_%s", appName))
                .baselineOnMigrate(Boolean.TRUE)
                .load();
        flyway.repair();
        return flyway;
    }


}
