package ntou.bernie.easylearn.user.represention;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import ntou.bernie.easylearn.user.core.User;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static ntou.bernie.easylearn.user.represention.UserDeserializer.userDeserilizer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by bernie on 2016/3/19.
 */
public class UserDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    @Test
    public void testUserDeserilizer() throws Exception {
        String fixture = fixture("user/user.json");
        User user = userDeserilizer(fixture, objectMapper);
        assertEquals("1009840175700426", user.getId());
        assertEquals("范振原", user.getName());
        assertEquals("全部的懶人包", user.getFolder().get(0).getName());
        assertEquals("allfolder", user.getFolder().get(0).getId());
        assertTrue(user.getFolder().get(0).getPack().contains("pack1439381800612"));
        assertEquals(1455851572000L, user.getSetting().getLastSyncTime());
        assertEquals(26, user.getSetting().getVersion());
    }

    @Test(expected = Exception.class)
    public void testUserDeserilizerValidation() throws Exception {
        String fixture = fixture("user/userFailure.json");

        User user = userDeserilizer(fixture, objectMapper);
    }

    @Test(expected = Exception.class)
    public void testUserDeserilizerValidation2() throws Exception {
        String fixture = fixture("user/userFailure2.json");

        User user = userDeserilizer(fixture, objectMapper);
    }

    @Test(expected = Exception.class)
    public void testUserDeserilizerValidation3() throws Exception {
        String fixture = fixture("user/userFailure3.json");

        User user = userDeserilizer(fixture, objectMapper);
    }

    @Test(expected = Exception.class)
    public void testUserDeserilizerValidation4() throws Exception {
        String fixture = fixture("user/userFailure4.json");

        User user = userDeserilizer(fixture, objectMapper);
    }

    @Test(expected = Exception.class)
    public void testUserDeserilizerValidation5() throws Exception {
        String fixture = fixture("user/userFailure4.json");

        User user = userDeserilizer(fixture, objectMapper);
    }
}