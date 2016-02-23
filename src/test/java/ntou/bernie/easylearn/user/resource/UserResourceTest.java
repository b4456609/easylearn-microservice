package ntou.bernie.easylearn.user.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import io.dropwizard.testing.junit.ResourceTestRule;
import ntou.bernie.easylearn.user.db.UserDAO;
import org.bson.types.ObjectId;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import ntou.bernie.easylearn.user.core.User;

public class UserResourceTest {

	private ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

	private static UserDAO userDAO = mock(UserDAO.class);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
			.addResource(new UserResource(userDAO))
			.build();

	@Test
	public void testUserJsonDeserialize() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"id\":\"1009840175700426\",\"name\":\"\u8303\u632F\u539F\",\"setting\":{\"wifi_sync\":true,\"mobile_network_sync\":true,\"last_sync_time\":1450325981000,\"modified\":true,\"version\":25},\"bookmark\":[],\"folder\":[]}";

		User user = objectMapper.readValue(json, User.class);		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<User>> constraintViolations =
			      validator.validate( user );
		assertEquals(0,constraintViolations.size());
	}

	@Test
	public void testSyncUser() throws IOException {
		String json = "{\"id\":\"1009840175700426\",\"name\":\"范振原\",\"setting\":{\"wifi_sync\":true,\"mobile_network_sync\":true,\"last_sync_time\":1450325981000,\"modified\":true,\"version\":25},\"bookmark\":[],\"folder\":[]}";

		User user = objectMapper.readValue(json, User.class);

		when(userDAO.isExist("1009840175700426")).thenReturn(false);
		//when(userDAO.save(user)).thenReturn("some");
		when(userDAO.getByUserId("1009840175700426")).thenReturn(user);

		Response result = resources.client().target("/user/sync").request().post(Entity.json(json));
		System.out.println(result);

	}

}
