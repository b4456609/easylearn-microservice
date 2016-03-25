package ntou.bernie.easylearn.image.resources;

import ntou.bernie.easylearn.image.core.ImgItem;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by bernie on 2016/3/23.
 */
@Path("/file")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImageResources {
    private Datastore datastore;
    ImageResources(Datastore datastore){
        this.datastore = datastore;
    }

    @POST
    public Response sync(ImgItem imgItem) {
        datastore.save(imgItem);
        return Response.ok().build();
    }
}
