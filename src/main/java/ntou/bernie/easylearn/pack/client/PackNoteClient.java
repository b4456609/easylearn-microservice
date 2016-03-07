package ntou.bernie.easylearn.pack.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import ntou.bernie.easylearn.note.resource.NoteResource;

import javax.ws.rs.container.ResourceContext;
import java.io.IOException;

/**
 * Created by bernie on 2016/2/26.
 */
public class PackNoteClient {

    private final ObjectMapper objectMapper;

    public PackNoteClient() {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    public void syncNote(String packJson, ResourceContext rc) {
        NoteResource noteResource = rc.getResource(NoteResource.class);
        noteResource.syncNote(packJson);
    }

    public JsonNode getNoteByVersionId(String versionId, ResourceContext rc) throws IOException {
        NoteResource noteResource = rc.getResource(NoteResource.class);
        JsonNode jsonNode = objectMapper.valueToTree(noteResource.getNote(versionId));
        return objectMapper.readTree(jsonNode.toString());
    }
}
