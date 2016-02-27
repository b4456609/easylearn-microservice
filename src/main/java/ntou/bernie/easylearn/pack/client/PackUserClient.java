package ntou.bernie.easylearn.pack.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import ntou.bernie.easylearn.user.resource.UserResource;

import javax.ws.rs.container.ResourceContext;
import java.util.List;

/**
 * Created by bernie on 2016/2/26.
 */
public class PackUserClient {

    private final ObjectMapper objectMapper;

    public PackUserClient() {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    public String getUserFolder(String userId, ResourceContext rc) {
        UserResource userResource = rc.getResource(UserResource.class);
        try {
            return objectMapper.writeValueAsString(userResource.getUserFolder(userId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getUserPacks(String userId, ResourceContext rc) {
        UserResource userResource = rc.getResource(UserResource.class);
        return userResource.getUserPack(userId);
    }
}
