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
        LOGGER.debug(syncJson);
        //sync user
        UserResource userResource = rc.getResource(UserResource.class);
        userResource.syncUser(syncJson);

        PackResource packResource = rc.getResource(PackResource.class);
        packResource.syncPacks(syncJson);
        return Response.ok().build();
    }
}
