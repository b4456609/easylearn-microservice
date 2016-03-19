package ntou.bernie.easylearn.user.represention;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ntou.bernie.easylearn.user.core.User;

/**
 * Created by bernie on 2016/3/19.
 */
public class UserSerializer {
    public static String userSerilizer(User user, ObjectMapper objectMapper) throws JsonProcessingException {
        ObjectNode userNode = objectMapper.valueToTree(user);
        JsonNode folderNode = userNode.get("folder");
        JsonNode settingNode = userNode.get("setting");
        userNode.remove("setting");
        userNode.remove("bookmark");
        userNode.remove("folder");

        ObjectNode respNode = objectMapper.createObjectNode();
        respNode.set("user", userNode);
        respNode.set("setting", settingNode);
        respNode.set("folder", folderNode);

        return objectMapper.writeValueAsString(respNode);
    }
}
