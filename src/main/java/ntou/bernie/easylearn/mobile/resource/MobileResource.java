package ntou.bernie.easylearn.mobile.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ntou.bernie.easylearn.pack.resource.PackResource;
import ntou.bernie.easylearn.user.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

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
        Response userResp = userResource.syncUser(syncJson);
        LOGGER.debug(userResp.toString());

        //sync pack
        PackResource packResource = rc.getResource(PackResource.class);
        Response packResp = packResource.syncPacks(syncJson);
        LOGGER.debug(packResp.toString());

        if (userResp.getStatus() != 200 || packResp.getStatus() != 200)
            return Response.serverError().build();

        String userJson = (String) userResp.getEntity();
        LOGGER.debug(userJson);

        try {
            ObjectNode respNode = (ObjectNode) objectMapper.readTree(userJson);
            String userId = respNode.get("user").get("id").textValue();
            Response packsResp = packResource.getUserPacks(userId);
            JsonNode packsNode = objectMapper.readTree((String) packsResp.getEntity());

            for (final JsonNode pack : packsNode) {
                respNode.set(pack.get("id").asText(), pack);
            }

            LOGGER.debug(respNode.toString());

            return Response.ok(respNode.toString()).build();

        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

    }
}
