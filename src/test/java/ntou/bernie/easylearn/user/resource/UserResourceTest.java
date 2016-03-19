package ntou.bernie.easylearn.user.resource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.dropwizard.testing.junit.ResourceTestRule;
import ntou.bernie.easylearn.user.core.User;
import ntou.bernie.easylearn.user.db.UserDAO;
import ntou.bernie.easylearn.user.represention.UserDeserializer;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UserResourceTest {

    private static UserDAO userDAO = mock(UserDAO.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(userDAO))
            .build();
    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    @After
    public void tearDown() {
        // we have to reset the mock after each test because of the
        // @ClassRule, or use a @Rule as mentioned below.
        reset(userDAO);
    }

    @Test
    public void testSyncUserNewUser() throws IOException {
        String json = fixture("user/user.json");
        User user = UserDeserializer.userDeserilizer(json, objectMapper);

        when(userDAO.isExist("1009840175700426")).thenReturn(false);
        when(userDAO.getByUserId("1009840175700426")).thenReturn(user);

        Response result = resources.client().target("/user/sync").request().post(Entity.json(json));
        String entity = result.readEntity(String.class);

        JsonNode responseJsonNode = objectMapper.readTree(json);
        JsonNode resultJsonNode = objectMapper.readTree(entity);

        verify(userDAO, times(1)).isExist(anyString());
        verify(userDAO, times(1)).getByUserId(anyString());
        verify(userDAO, times(1)).save(any());
        verify(userDAO, never()).sync(any());
        assertTrue(responseJsonNode.equals(resultJsonNode));
    }

    @Test
    public void testSyncUserNewDevice() throws IOException {
        String json = fixture("user/userNewUser.json");
        User user = UserDeserializer.userDeserilizer(json, objectMapper);

        when(userDAO.isExist("1009840175700426")).thenReturn(true);
        when(userDAO.getByUserId("1009840175700426")).thenReturn(user);

        Response result = resources.client().target("/user/sync").request().post(Entity.json(json));
        String entity = result.readEntity(String.class);

        JsonNode responseJsonNode = objectMapper.readTree(json);
        JsonNode resultJsonNode = objectMapper.readTree(entity);

        verify(userDAO, times(1)).isExist(anyString());
        verify(userDAO, times(1)).getByUserId(anyString());
        verify(userDAO, never()).save(any());
        verify(userDAO, never()).sync(any());
        assertTrue(responseJsonNode.equals(resultJsonNode));
    }

    @Test
    public void testSyncUserConflict() throws IOException {
        String json = fixture("user/user.json");
        User user = UserDeserializer.userDeserilizer(json, objectMapper);

        when(userDAO.isExist("1009840175700426")).thenReturn(true);
        when(userDAO.isConflict(any())).thenReturn(true);
        when(userDAO.getByUserId("1009840175700426")).thenReturn(user);

        Response result = resources.client().target("/user/sync").request().post(Entity.json(json));
        verify(userDAO, times(1)).isExist(anyString());
        verify(userDAO, times(1)).isConflict(any());
        verify(userDAO, never()).sync(any());
        assertEquals(Response.Status.CONFLICT.getStatusCode(), result.getStatus());
    }

    @Test
    public void testSyncUserSync() throws IOException {
        String json = fixture("user/user.json");
        User user = UserDeserializer.userDeserilizer(json, objectMapper);

        when(userDAO.isExist("1009840175700426")).thenReturn(true);
        when(userDAO.isConflict(any())).thenReturn(false);
        when(userDAO.getByUserId("1009840175700426")).thenReturn(user);

        Response result = resources.client().target("/user/sync").request().post(Entity.json(json));
        verify(userDAO, times(1)).isExist(anyString());
        verify(userDAO, times(1)).isConflict(any());
        verify(userDAO, times(1)).sync(any());
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }

    @Test
    public void testGetUserPack() throws Exception {
        String json = fixture("user/user.json");
        User user = UserDeserializer.userDeserilizer(json, objectMapper);
        when(userDAO.getByUserId(any())).thenReturn(user);

        Response result = resources.client().target("/user/afewafe/pack").request().get();
        String entity = result.readEntity(String.class);
        String except = "[\n" +
                "        \"pack1439355907459\",\n" +
                "        \"pack1439367500493\",\n" +
                "        \"pack1439370245981\",\n" +
                "        \"pack1439372921598\",\n" +
                "        \"pack1439381800612\",\n" +
                "        \"pack1439385129482\",\n" +
                "        \"pack1439394796784\",\n" +
                "        \"pack1439451391246\",\n" +
                "        \"pack1439471856230\"\n" +
                "      ]";
        assertEquals(objectMapper.readTree(entity), objectMapper.readTree(except));
    }
}
