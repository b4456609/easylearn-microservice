/**
 * 
 */
package ntou.bernie.easylearn.note.resource;

import java.io.IOException;
import java.net.URI;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.mongodb.DuplicateKeyException;

import ntou.bernie.easylearn.note.core.Note;

/**
 * @author bernie
 *
 */
@Path("/note")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteResource {
	private Datastore datastore;
	private static final Logger LOGGER = LoggerFactory.getLogger(NoteResource.class);
	
	@Context
    UriInfo uriInfo;

	public NoteResource(Datastore datastore) {
		this.datastore = datastore;
	}

	
	@GET
	@Path("/{versionId}")
	@Timed
	public Note getNote(@PathParam("versionId") String versionId) {
		Note note = datastore.createQuery(Note.class).field("versionId").equal(versionId).get();		
		if(note == null)
			throw new WebApplicationException(404);		
		return note;
	}

	@POST
	@Timed
	public Response addNote(String noteJson) {
		try {
			ObjectMapper mapper = new ObjectMapper()
					.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
			Note note;
			note = mapper.readValue(noteJson, Note.class);
			datastore.save(note);
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			URI userUri = ub.
	                path("fds").
	                build();
			return Response.created(userUri).build();
			
		} catch (IOException e) {
			LOGGER.warn("Json style error",e);
			return Response.status(Response.Status.BAD_REQUEST).build();	
		} catch (DuplicateKeyException e){
			LOGGER.warn("DuplicateKeyException",e);
			return Response.status(Response.Status.CONFLICT).build();
		}
	}
}
