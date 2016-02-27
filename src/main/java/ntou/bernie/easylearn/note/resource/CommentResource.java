package ntou.bernie.easylearn.note.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ntou.bernie.easylearn.note.core.Comment;
import ntou.bernie.easylearn.note.core.Note;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Path("/comment/{noteId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentResource.class);
    @Context
    UriInfo uriInfo;
    private Datastore datastore;

    public CommentResource(Datastore datastore) {
        this.datastore = datastore;
    }

    @GET
    @Timed
    public List<Comment> getCommentByNoteId(@PathParam("noteId") String noteId) {
        Note note = datastore.createQuery(Note.class).field("id").equal(noteId).get();
        if (note == null) {
            throw new WebApplicationException(404);
        }
        return note.getComment();
    }

    @POST
    @Timed
    public Response addComment(@PathParam("noteId") String noteId, String commentJson) {
        if (commentJson == null)
            throw new WebApplicationException(400);

        try {
            // map to comment object
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Comment comment;
            comment = objectMapper.readValue(commentJson, Comment.class);

            // query note object
            final Query<Note> noteQuery = datastore.createQuery(Note.class).field("id").equal(noteId);
            // operation on query
            final UpdateOperations<Note> updateOperations = datastore.createUpdateOperations(Note.class).add("comments",
                    comment);
            // update db
            datastore.update(noteQuery, updateOperations);
        } catch (IOException e) {
            LOGGER.info("json pharse problem", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI userUri = ub.build();

        return Response.created(userUri).build();
    }
}
