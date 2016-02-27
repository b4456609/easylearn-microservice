package ntou.bernie.easylearn.pack.resource;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import ntou.bernie.easylearn.pack.client.PackNoteClient;
import ntou.bernie.easylearn.pack.client.PackUserClient;
import ntou.bernie.easylearn.pack.db.PackDAO;
import ntou.bernie.easylearn.user.core.User;
import ntou.bernie.easylearn.user.db.UserDAO;
import ntou.bernie.easylearn.user.resource.UserResource;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import ntou.bernie.easylearn.pack.core.Pack;
import ntou.bernie.easylearn.pack.core.Version;

@Path("/pack")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PackResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PackResource.class);
    private final PackDAO packDAO;
    private final ObjectMapper objectMapper;
    private final PackUserClient packUserClient;
    private final PackNoteClient packNoteClient;
    @Context
    UriInfo uriInfo;


    /**
     * @param packDAO
     */
    public PackResource(PackDAO packDAO) {
        this.packDAO = packDAO;
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        packUserClient = new PackUserClient();
        packNoteClient = new PackNoteClient();

    }

    public PackResource(PackDAO packDAO, PackUserClient packUserClient, PackNoteClient packNoteClient) {
        this.packDAO = packDAO;
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        this.packUserClient = packUserClient;
        this.packNoteClient = packNoteClient;
    }

    @GET
    @Path("/{packId}")
    @Timed
    public Pack getPack(@PathParam("packId") String packId) {
        // query from db
        Pack pack = packDAO.getPackById(packId);
        if (pack == null)
            throw new WebApplicationException(404);
        return pack;
    }

    @GET
    @Path("/{packId}/version")
    @Timed
    public List<Version> getPackVersion(@PathParam("packId") String packId) {
        // query from db
        Pack pack = packDAO.getPackById(packId);
        if (pack == null)
            throw new WebApplicationException(404);
        return pack.getVersion();
    }

    @POST
    @Timed
    public Response addPack(String packJson) {
        try {
            // map to comment object
            Pack pack = objectMapper.readValue(packJson, Pack.class);

            // pack json validation
            packValidation(pack);

            // save to db
            packDAO.save(pack);

            // build response
            URI userUri = uriInfo.getAbsolutePathBuilder().path(pack.getId()).build();
            return Response.created(userUri).build();
        } catch (IOException e) {
            LOGGER.info("json pharse problem", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/sync")
    @Timed
    public Response syncPacks(String syncJson) {
        try {

            JsonNode syncJsonNode = objectMapper.readTree(syncJson);
            Iterator<Map.Entry<String, JsonNode>> fields = syncJsonNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> item = fields.next();
                if (item.getKey().contains("pack")) {
                    String packJson = item.getValue().toString();
                    // map to comment object
                    Pack pack = objectMapper.readValue(packJson, Pack.class);
                    pack.setId(item.getKey());
                    // pack json validation
                    packValidation(pack);

                    // sync pack
                    packDAO.sync(pack);
                    // sync note
                    packNoteClient.syncNote(packJson);
                }
            }

            //get user's pack
            String userId = syncJsonNode.get("user").get("id").toString();
            String folderJson = packUserClient.getUserFolder(userId);
            List<String> packIds= JsonPath.parse(folderJson).read("$..pack[*]", List.class);
            List<Pack> packs = packDAO.getPacksById(packIds);

            // build response
            return Response.ok(packs).build();
        } catch (IOException e) {
            LOGGER.info("json pharse problem" + e);
            e.printStackTrace();
            System.out.println();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/sync/single")
    @Timed
    public Response syncPack(String packJson) {
        try {
            // map to comment object
            Pack pack = objectMapper.readValue(packJson, Pack.class);

            // pack json validation
            packValidation(pack);

            // sync pack
            packDAO.sync(pack);

            // build response
            URI userUri = uriInfo.getAbsolutePathBuilder().path(pack.getId()).build();
            return Response.created(userUri).build();
        } catch (IOException e) {
            LOGGER.info("json pharse problem" + e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private void packValidation(Pack pack) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //json validation
        Set<ConstraintViolation<Pack>> constraintViolations = validator.validate(pack);
        for (ConstraintViolation<Pack> constraintViolation : constraintViolations) {
            LOGGER.warn(constraintViolation.toString());
        }
        if (constraintViolations.size() > 0) {
            //json validation fail
            LOGGER.warn("json validation fail");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
