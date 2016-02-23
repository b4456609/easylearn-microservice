/**
 * 
 */
package ntou.bernie.easylearn.user.resource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import ntou.bernie.easylearn.user.core.Folder;
import ntou.bernie.easylearn.user.db.UserDAOImp;
import org.joda.time.DateTime;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import ntou.bernie.easylearn.user.core.User;
import ntou.bernie.easylearn.user.db.UserDAO;

/**
 * @author bernie
 *
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
	private final UserDAO userDAO;


	/**
	 * @param datastore
	 */
	public UserResource(Datastore datastore) {
		this.userDAO = new UserDAOImp(User.class,datastore);
	}


	@POST
	@Timed
	public Response syncUser(String userJson) {
		LOGGER.debug("Request content",userJson);
		if (userJson == null)
			throw new WebApplicationException(400);
		try {
			LOGGER.info("map to user object");
			// map to comment object
			ObjectMapper objectMapper = new ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
			User user = objectMapper.readValue(userJson, User.class);
			List<Folder> folder = objectMapper.readValue(userJson, new TypeReference<List<Folder>>(){});
			user.setFolder(folder);
			
			//validation json
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<User>> constraintViolations =
				      validator.validate( user );
			if(constraintViolations.size() > 0){
				LOGGER.info("validation not pass");
				for(ConstraintViolation<User> violation: constraintViolations){
					LOGGER.info(violation.toString());
				}
				throw new WebApplicationException(Response.Status.BAD_REQUEST);
			}
			
			// check if new user 
			if(!userDAO.isExist(user.getId())){
				LOGGER.info("user not exist");
				user.setCreateTime(new DateTime().getMillis());
				user.setLastUpTime(new DateTime().getMillis());
				userDAO.save(user);
				//return user object
				User userResponse = datastore.createQuery(User.class).field("id").equal(user.getId()).get();
				String userResponseJson = objectMapper.writeValueAsString(userResponse);
				return Response.ok(userResponseJson ,MediaType.APPLICATION_JSON).build();
			}
			
			//check data conflict
			if(user.isConflict(datastore)){
				LOGGER.info("conflict");
				throw new WebApplicationException(Response.Status.CONFLICT);
			}

			LOGGER.info("sync to db");
			user.sync(datastore);
			//return user object
			LOGGER.info("Build response");
			User userResponse = datastore.createQuery(User.class).field("id").equal(user.getId()).get();
			String userResponseJson = objectMapper.writeValueAsString(userResponse);
			return Response.ok(userResponseJson ,MediaType.APPLICATION_JSON).build();
			
		} catch (IOException e) {
			LOGGER.warn("json error", e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

}
