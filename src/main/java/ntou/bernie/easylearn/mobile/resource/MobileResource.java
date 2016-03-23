package ntou.bernie.easylearn.mobile.resource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ntou.bernie.easylearn.mobile.core.ImgItem;
import ntou.bernie.easylearn.pack.resource.PackResource;
import ntou.bernie.easylearn.user.resource.UserResource;
import org.mongodb.morphia.Datastore;
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
@Path("/file")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MobileResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileResource.class);
    @Context
    private ResourceContext rc;
    private Datastore datastore;
    MobileResource(Datastore datastore){
        this.datastore = datastore;
    }

    @POST
    public Response sync(ImgItem imgItem) {
        datastore.save(imgItem);
        return Response.ok().build();
    }
}
