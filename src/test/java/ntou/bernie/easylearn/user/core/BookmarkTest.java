package ntou.bernie.easylearn.user.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.junit.Test;

import java.io.IOException;

public class BookmarkTest {

    @Test
    public void test() throws JsonParseException, JsonMappingException, IOException {
        String json = "{\"id\":\"id\",\"name\":\"fasdfa\"}";

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        Bookmark bookmark = objectMapper.readValue(json, Bookmark.class);
        System.out.println(bookmark);
    }

}
