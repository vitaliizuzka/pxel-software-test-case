package software.pxel.pxelsoftwaretestcase.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    private Flyway flyway;
    @Autowired
    public FlywayConfig(Flyway flyway) {
        this.flyway = flyway;
    }
    @PostConstruct
    public void cleanAndMigrate() {
        flyway.clean();
        flyway.migrate();
    }
}