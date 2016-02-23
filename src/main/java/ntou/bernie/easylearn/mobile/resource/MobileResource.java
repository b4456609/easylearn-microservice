package ntou.bernie.easylearn.mobile.resource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ntou.bernie.easylearn.note.resource.NoteResource;
import ntou.bernie.easylearn.pack.resource.PackResource;
import ntou.bernie.easylearn.user.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by bernie on 2016/2/19.
 */
@Path("/sync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MobileResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileResource.class);
    @Context
    private ResourceContext rc;

    @POST
    public Response sync(String syncJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(syncJson);
            ObjectNode userNode = (ObjectNode) jsonNode.get("user");
            userNode.set("folder", jsonNode.get("folder"));
            // need to implement!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            userNode.putArray("bookmark");



            //sync user
            UserResource userResource = rc.getResource(UserResource.class);
            userResource.syncUser(userNode.toString());
            LOGGER.debug(userNode.toString());

            PackResource packResource = rc.getResource(PackResource.class);
            NoteResource noteResource = rc.getResource(NoteResource.class);

            Iterator<String> elements = jsonNode.fieldNames();
            while (elements.hasNext()) {
                String packNode = elements.next();
                LOGGER.debug(packNode);
                if (packNode.contains("pack")) {
                    ObjectNode pack = (ObjectNode) jsonNode.get(packNode);
                    pack.put("id", packNode);
                    LOGGER.debug(pack.toString());
                    packResource.syncPack(pack.toString());
                    noteResource.syncNote(pack.toString());
                }
            }
            return Response.ok().build();

        } catch (IOException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
