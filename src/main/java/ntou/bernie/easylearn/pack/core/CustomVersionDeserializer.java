package ntou.bernie.easylearn.pack.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.util.List;

/**
 * Created by bernie on 2016/2/28.
 */
public class CustomVersionDeserializer extends JsonDeserializer<Version> {
    @Override
    public Version deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        Version version = objectMapper.readValue(p,Version.class);
        System.out.println(version);
        System.out.println(p.toString());
        List<String> notesId = JsonPath.parse(p.readValueAsTree().).read("$.note[*].id");
        version.setNoteId(notesId);
        return version;
    }
}
