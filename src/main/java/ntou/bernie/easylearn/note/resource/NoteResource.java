/**
 *
 */
package ntou.bernie.easylearn.note.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.mongodb.DuplicateKeyException;
import ntou.bernie.easylearn.note.core.Note;
import ntou.bernie.easylearn.note.db.NoteDAO;
import ntou.bernie.easylearn.note.representation.NotesDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

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
    private NoteDAO noteDAO;

    public NoteResource(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }


    @GET
    @Path("/{versionId}")
    @Timed
    public List<Note> getNote(@PathParam("versionId") String versionId) {
        List<Note> note = noteDAO.getNotesByVersionId(versionId);
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
    public Response syncNotes(String packsJson) {
        try {
            LOGGER.debug(packsJson);

            NotesDeserializer notesDeserializer = new NotesDeserializer();
            final List<Note> notes = notesDeserializer.deserializer(packsJson, mapper);


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

}
