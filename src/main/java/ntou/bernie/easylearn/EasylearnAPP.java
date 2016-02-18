/**
 * 
 */
package ntou.bernie.easylearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import ntou.bernie.easylearn.db.MorphiaService;
import ntou.bernie.easylearn.health.DatabaseHealthCheck;
import ntou.bernie.easylearn.note.resource.CommentResource;
import ntou.bernie.easylearn.note.resource.NoteResource;
import ntou.bernie.easylearn.user.resource.UserResource;

/**
 * @author bernie
 *
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

		// mongodb driver
		MorphiaService morphia = new MorphiaService(configuration.getDatabaseConfiguration());

		UserResource userResource = new UserResource(morphia.getDatastore());
		environment.jersey().register(userResource);

		NoteResource noteResource = new NoteResource(morphia.getDatastore());
		environment.jersey().register(noteResource);
		
		CommentResource commentResource = new CommentResource(morphia.getDatastore());
		environment.jersey().register(commentResource);

		// database health check
		environment.healthChecks().register("database",
				new DatabaseHealthCheck(configuration.getDatabaseConfiguration()));

	}

}
