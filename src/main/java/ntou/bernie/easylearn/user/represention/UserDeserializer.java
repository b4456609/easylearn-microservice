package ntou.bernie.easylearn.user.represention;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ntou.bernie.easylearn.user.core.Bookmark;
import ntou.bernie.easylearn.user.core.Folder;
import ntou.bernie.easylearn.user.core.Setting;
import ntou.bernie.easylearn.user.core.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by bernie on 2016/3/19.
 */
public class UserDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeserializer.class);

    public static User userDeserilizer(String userJson, ObjectMapper objectMapper) throws IOException {

        JsonNode jsonNode = objectMapper.readTree(userJson);

        //user json
        JsonNode userJsonNode = jsonNode.get("user");
        User user = objectMapper.readValue(userJsonNode.toString(), User.class);

        //folder json
        JsonNode folderJsonNode = jsonNode.get("folder");
        List<Folder> folder = objectMapper.readValue(folderJsonNode.toString(), new TypeReference<List<Folder>>() {
        });
        user.setFolder(folder);

        //setting
        JsonNode settingJsonNode = jsonNode.get("setting");
        Setting setting = objectMapper.readValue(settingJsonNode.toString(), Setting.class);

        user.setSetting(setting);

        //bookmark json
        Bookmark bookmark = new Bookmark();
        user.setBookmark(new ArrayList());
//            String bookmarkJson = extractBookmark(userJson);
//            List<Bookmark> bookmark = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<Bookmark>>() {
//            });
//            user.setBookmark(bookmark);


        //validation json
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> constraintViolations =
                validator.validate(user);
        if (constraintViolations.size() > 0) {
            LOGGER.info("validation not pass");
            for (ConstraintViolation<User> violation : constraintViolations) {
                LOGGER.info(violation.toString());
            }
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return user;
    }
}
