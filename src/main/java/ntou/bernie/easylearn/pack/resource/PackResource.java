package ntou.bernie.easylearn.pack.resource;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
	private Datastore datastore;
	private ObjectMapper objectMapper;
	@Context
	UriInfo uriInfo;

	/**
	 * @param datastore
	 */
	public PackResource(Datastore datastore) {
		this.datastore = datastore;
		objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	}

	@GET
	@Path("/{packId}")
	@Timed
	public Pack getPack(@PathParam("packId") String packId) {
		// query from db
		Pack pack = datastore.createQuery(Pack.class).field("id").equal(packId).get();
		if (pack == null)
			throw new WebApplicationException(404);
		return pack;
	}

	@GET
	@Path("/{packId}/version")
	@Timed
	public List<Version> getPackVersion(@PathParam("packId") String packId) {
		// query from db
		Pack pack = datastore.createQuery(Pack.class).field("id").equal(packId).get();
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
			datastore.save(pack);

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
	public Response syncPack(String packJson) {
		try {
			// map to comment object
			Pack pack = objectMapper.readValue(packJson, Pack.class);

			// pack json validation
			packValidation(pack);

            // sync pack
            pack.sync(datastore);

			// build response
			URI userUri = uriInfo.getAbsolutePathBuilder().path(pack.getId()).build();
			return Response.created(userUri).build();
		} catch (IOException e) {
			LOGGER.info("json pharse problem" + e);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	private void packValidation(Pack pack){
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		//json validation
		Set<ConstraintViolation<Pack>> constraintViolations = validator.validate(pack);
		for(ConstraintViolation<Pack> constraintViolation:constraintViolations){
			LOGGER.warn(constraintViolation.toString());
		}
		if (constraintViolations.size() > 0){
			//json validation fail
			LOGGER.warn("json validation fail");
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
}
