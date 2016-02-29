/**
 *
 */
package ntou.bernie.easylearn.user.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import ntou.bernie.easylearn.user.core.Bookmark;
import ntou.bernie.easylearn.user.core.Folder;
import ntou.bernie.easylearn.user.core.User;
import ntou.bernie.easylearn.user.db.UserDAO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bernie
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Timed
    @Path("/{id}/folder")
    public List<Folder> getUserFolder(@PathParam("id") String userId) {
        if (userId == null)
            throw new WebApplicationException(400);
        return userDAO.getByUserId(userId).getFolder();
    }

    @GET
    @Timed
    @Path("/{id}/pack")
    public List<String> getUserPack(@PathParam("id") String userId) {
        if (userId == null)
            throw new WebApplicationException(400);
        List<Folder> folders = userDAO.getByUserId(userId).getFolder();
        List<String> packIds = new ArrayList<>();
        for (Folder folder : folders) {
            packIds.addAll(folder.getPack());
        }
        return packIds;
    }


    @POST
    @Timed
    @Path("/sync")
    public Response syncUser(String userJson) {
        LOGGER.debug("Request content", userJson);
        if (userJson == null)
            throw new WebApplicationException(400);
        try {
            LOGGER.info("map to user object");
            // map to comment object
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

            JsonNode jsonNode = objectMapper.readTree(userJson);

            //user json
            JsonNode userJsonNode = jsonNode.get("user");
            User user = objectMapper.readValue(userJsonNode.toString(), User.class);

            //folder json
            JsonNode folderJsonNode = jsonNode.get("folder");
            List<Folder> folder = objectMapper.readValue(folderJsonNode.toString(), new TypeReference<List<Folder>>() {
            });
            user.setFolder(folder);

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

            // check if new user
            if (!userDAO.isExist(user.getId())) {
                LOGGER.info("user not exist");
                user.setCreateTime(new DateTime().getMillis());
                user.setLastUpTime(new DateTime().getMillis());
                userDAO.save(user);
            }
            else if (user.getSetting().getVersion() == 0){
                //old user new login or new device
                // no need to do anything
                // direct to build response

            }
            //check data conflict
            else if (userDAO.isConflict(user)) {
                LOGGER.info("conflict");
                throw new WebApplicationException(Response.Status.CONFLICT);
            } else {

                LOGGER.info("sync to db");
                userDAO.sync(user);
            }
            //response user json
            LOGGER.info("Build response");
            User respUser = userDAO.getByUserId(user.getId());
            ObjectNode userNode = objectMapper.valueToTree(respUser);
            JsonNode folderNode = userNode.get("folder");
            userNode.remove("bookmark");
            userNode.remove("folder");

            ObjectNode respNode = objectMapper.createObjectNode();
            respNode.set("user", userNode);
            respNode.set("folder", folderNode);

            String reponseJson = objectMapper.writeValueAsString(respNode);
            LOGGER.debug(reponseJson);
            return Response.ok(reponseJson).build();

        } catch (IOException e) {
            LOGGER.warn("json error", e);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    private String extractBookmark(String userJson) throws IOException {
        //bookmark json
        List<Map<String, Object>> versions = JsonPath.read(userJson, "$..version[*]");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode bookmarksJsonNode = objectMapper.createArrayNode();


        for (Map<String, Object> map : versions) {
            System.out.println(map.keySet());

            System.out.println(map.get("bookmark").toString());
            System.out.println(map.get("id").toString());
        }
        return bookmarksJsonNode.toString();
    }

}
