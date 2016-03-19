package ntou.bernie.easylearn.pack.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import org.mongodb.morphia.annotations.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by bernie on 2016/2/28.
 */
public class CustomVersionDeserializer extends JsonDeserializer<Version> {
    @Transient
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomVersionDeserializer.class);

    @Override
    public Version deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        LOGGER.debug(node.toString());
        List<String> notesId = JsonPath.parse(node.toString()).read("$.note[*].id");
        LOGGER.debug(notesId.toString());

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        ((ObjectNode) node).remove("note");
        Version version = objectMapper.readValue(node.toString(), Version.class);
        version.setNote(notesId);
        LOGGER.debug(version.toString());
        return version;
    }
}
