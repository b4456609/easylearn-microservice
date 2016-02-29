package ntou.bernie.easylearn.pack.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ntou.bernie.easylearn.pack.client.PackNoteClient;
import ntou.bernie.easylearn.pack.client.PackUserClient;
import ntou.bernie.easylearn.pack.core.CustomVersionDeserializer;
import ntou.bernie.easylearn.pack.core.Pack;
import ntou.bernie.easylearn.pack.core.Version;
import ntou.bernie.easylearn.pack.db.PackDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Context
    private ResourceContext rc;

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
    @Path("/user/{userId}")
    @Timed
    public Response getUserPacks(@PathParam("userId") String userId) {
        if (userId == null)
            throw new WebApplicationException(404);

        LOGGER.debug(userId);
        List<String> userPacks = packUserClient.getUserPacks(userId, rc);

        LOGGER.debug(userPacks.toString());

        try {
            // query from db
            List<Pack> packs = packDAO.getPacksById(userPacks);
            LOGGER.debug(packs.toString());
            String json = objectMapper.writeValueAsString(packs);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WebApplicationException(500);
        }
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
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Version.class, new CustomVersionDeserializer());
        objectMapper.registerModule(module);
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
                    packNoteClient.syncNote(packJson, rc);
                }
            }

            // build response
            return Response.ok().build();
        } catch (IOException e) {
            LOGGER.info("json pharse problem" + e);
            e.printStackTrace();
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
