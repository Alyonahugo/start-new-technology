package com.test.rmmfl.config;

import com.test.gdo.annotation.ToolsDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayInitializer implements InitializingBean {

    @Value("${flyway.db.clean:false}")
    boolean clean;

    @Autowired
    @ToolsDataSource
    DataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("db/migration");

        if (clean) {
            flyway.clean();
        }

        flyway.migrate();
    }
}
