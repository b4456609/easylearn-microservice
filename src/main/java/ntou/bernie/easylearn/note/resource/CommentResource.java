package ntou.bernie.easylearn.note.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ntou.bernie.easylearn.note.core.Comment;
import ntou.bernie.easylearn.note.core.Note;
import ntou.bernie.easylearn.note.db.NoteDAO;
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
    private NoteDAO noteDAO;

    public CommentResource(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    @GET
    @Timed
    public List<Comment> getCommentByNoteId(@PathParam("noteId") String noteId) {
        List<Comment> comments = noteDAO.getCommentsByNoteId(noteId);
        if (comments == null) {
            throw new WebApplicationException(404);
        }
        return comments;
    }

    @POST
    @Timed
    public Response addComment(@PathParam("noteId") String noteId, String commentJson) {
        if (commentJson == null)
            throw new WebApplicationException(400);

        try {
            // map to comment object
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Comment comment = objectMapper.readValue(commentJson, Comment.class);
            comment.setNoteId(noteId);

            noteDAO.addComment(noteId, comment);

        } catch (IOException e) {
            LOGGER.info("json pharse problem", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI userUri = ub.build();

        return Response.created(userUri).build();
    }
}
