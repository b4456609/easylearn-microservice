package ntou.bernie.easylearn.user.represention;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import ntou.bernie.easylearn.user.core.Folder;
import ntou.bernie.easylearn.user.core.Setting;
import ntou.bernie.easylearn.user.core.User;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertTrue;

/**
 * Created by bernie on 2016/3/19.
 */
public class UserSerializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    @Test
    public void testUserSerilizer() throws Exception {
        Setting setting = new Setting(true, true, 26, false, 1455851572000L);
        String[] strings = {"pack1439355907459",
                "pack1439367500493",
                "pack1439370245981",
                "pack1439372921598",
                "pack1439381800612",
                "pack1439385129482",
                "pack1439394796784",
                "pack1439451391246",
                "pack1439471856230"};
        Folder folder = new Folder("allfolder", "全部的懶人包", Arrays.asList(strings));
        User user = new User("1009840175700426", "范振原", setting, Collections.singletonList(folder), null);
        String userStr = UserSerializer.userSerilizer(user, objectMapper);
        String userjson = fixture("user/user.json");

        assertTrue(objectMapper.readTree(userStr).equals(objectMapper.readTree(userjson)));
    }
}