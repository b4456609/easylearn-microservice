package ntou.bernie.easylearn.health;

import com.codahale.metrics.health.HealthCheck;
import ntou.bernie.easylearn.DatabaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHealthCheck extends HealthCheck {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthCheck.class);
    private final DatabaseConfiguration databaseConfiguration;

    /**
     *
     */
    public DatabaseHealthCheck(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    @Override
    protected Result check() throws Exception {
        try {
            databaseConfiguration.getMongoClient().getAddress();
        } catch (Exception e) {
            log.info("can't connect to database" + e);
            return Result.unhealthy("can't connect to database");
        }
        return Result.healthy();
    }

}
