/**
 *
 */
package ntou.bernie.easylearn.note.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.DuplicateKeyException;
import ntou.bernie.easylearn.note.core.Note;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author bernie
 */
@Path("/note")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteResource.class);
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    @Context
    UriInfo uriInfo;
    private Datastore datastore;

    public NoteResource(Datastore datastore) {
        this.datastore = datastore;
    }


    @GET
    @Path("/{versionId}")
    @Timed
    public List<Note> getNote(@PathParam("versionId") String versionId) {
        List<Note> note = datastore.createQuery(Note.class).field("versionId").equal(versionId).asList();
        if (note == null)
            throw new WebApplicationException(404);
        LOGGER.debug("getNote " + versionId + note.toString());
        return note;
    }

/*    @POST
    @Timed
    public Response addNote(String noteJson) {
        try {
            Note note;
            note = mapper.readValue(noteJson, Note.class);
            datastore.save(note);
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI userUri = ub.
                    path("fds").
                    build();
            return Response.created(userUri).build();

        } catch (IOException e) {
            LOGGER.warn("Json style error", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (DuplicateKeyException e) {
            LOGGER.warn("DuplicateKeyException", e);
            return Response.status(Response.Status.CONFLICT).build();
        }
    }*/

    @POST
    @Path("/sync")
    @Timed
    public Response syncNote(String packJson) {
        try {
            LOGGER.debug(packJson);

            //get pack id
            JsonNode jsonNode = mapper.readTree(packJson);
            Iterator<JsonNode> versionsNode = jsonNode.get("version").elements();
            List<Note> notesArray = new ArrayList<Note>();

            while (versionsNode.hasNext()) {
                JsonNode version = versionsNode.next();
                String versionId = version.get("id").asText();
                Iterator<JsonNode> notes = version.get("note").elements();
                while (notes.hasNext()) {
                    ObjectNode noteJsonNode = (ObjectNode) notes.next();
                    noteJsonNode.put("version_id", versionId);
                    Note note = mapper.readValue(noteJsonNode.toString(), Note.class);
                    notesArray.add(note);
                }
            }

//            //json path get pack's note
//            ReadContext ctx = JsonPath.parse(packJson);
//            List<String> notesJson = ctx.read("$.version[*].note.*");
//
//            //map to class
//            List<Note> notes = mapper.readValue(notesJson.toString(), new TypeReference<List<Note>>() {
//            });

            LOGGER.debug(notesArray.toString());
            for (Note note : notesArray) {
                if (isValid(note)) {
                    LOGGER.debug("Note is valid " + note);
                    note.sync(datastore);
                } else {
                    LOGGER.debug("Note is not valid " + note);
                }
                //not yet implement
            }

            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI userUri = ub.
                    path("fds").
                    build();
            return Response.created(userUri).build();

        } catch (IOException e) {
            LOGGER.warn("Json style error", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (DuplicateKeyException e) {
            LOGGER.warn("DuplicateKeyException", e);
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    private boolean isValid(Note note) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //json validation
        Set<ConstraintViolation<Note>> constraintViolations = validator.validate(note);
        for (ConstraintViolation<Note> constraintViolation : constraintViolations) {
            LOGGER.warn(constraintViolation.toString());
        }
        if (constraintViolations.size() > 0) {
            return false;
        }
        return true;
    }
}
