/**
 *
 */
package ntou.bernie.easylearn;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import ntou.bernie.easylearn.db.MorphiaService;
import ntou.bernie.easylearn.health.DatabaseHealthCheck;
import ntou.bernie.easylearn.mobile.resource.MobileResource;
import ntou.bernie.easylearn.note.core.Note;
import ntou.bernie.easylearn.note.db.NoteDAOImp;
import ntou.bernie.easylearn.note.resource.CommentResource;
import ntou.bernie.easylearn.note.resource.NoteResource;
import ntou.bernie.easylearn.pack.core.Pack;
import ntou.bernie.easylearn.pack.db.PackDAOImp;
import ntou.bernie.easylearn.pack.resource.PackResource;
import ntou.bernie.easylearn.user.core.User;
import ntou.bernie.easylearn.user.db.UserDAOImp;
import ntou.bernie.easylearn.user.resource.UserResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * @author bernie
 */
public class EasylearnAPP extends Application<EasylearnAPPConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasylearnAPP.class);

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new EasylearnAPP().run(args);

    }

    @Override
    public void run(EasylearnAPPConfiguration configuration, Environment environment) throws Exception {
        LOGGER.info("Application name: {}", configuration.getAppName());
        environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");


        // mongodb driver
        MorphiaService morphia = new MorphiaService(configuration.getDatabaseConfiguration());

        UserResource userResource = new UserResource(new UserDAOImp(User.class, morphia.getDatastore()));
        environment.jersey().register(userResource);

        NoteResource noteResource = new NoteResource(new NoteDAOImp(Note.class, morphia.getDatastore()));
        environment.jersey().register(noteResource);

        CommentResource commentResource = new CommentResource(morphia.getDatastore());
        environment.jersey().register(commentResource);

        PackResource packResource = new PackResource(new PackDAOImp(Pack.class, morphia.getDatastore()));
        environment.jersey().register(packResource);

        MobileResource mobileResource = new MobileResource();
        environment.jersey().register(mobileResource);

        // database health check
        environment.healthChecks().register("database",
                new DatabaseHealthCheck(configuration.getDatabaseConfiguration()));

    }

}
