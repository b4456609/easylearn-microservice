package ntou.bernie.easylearn.note.resource;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ntou.bernie.easylearn.note.core.Comment;
import ntou.bernie.easylearn.note.core.Note;

@Path("/comment/{noteId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentResource.class);
	private Datastore datastore;
	@Context
	UriInfo uriInfo;

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
		return note.getComments();
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
