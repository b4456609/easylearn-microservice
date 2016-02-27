package ntou.bernie.easylearn.pack.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import ntou.bernie.easylearn.note.resource.NoteResource;
import ntou.bernie.easylearn.user.resource.UserResource;

import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;

/**
 * Created by bernie on 2016/2/26.
 */
public class PackNoteClient {
    @Context
    private ResourceContext rc;
    private final ObjectMapper objectMapper;

    public PackNoteClient() {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    public void syncNote(String packJson){
        NoteResource noteResource = rc.getResource(NoteResource.class);
        noteResource.syncNote(packJson);
    }
}
