package ntou.bernie.easylearn.note.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CommentTest {

    @Test
    public void whenDeserializingUsingJsonCreator_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
        String json = "{\"id\":\"123\",\"content\":\"name\",\"create_time\":\"2016-02-13T23:16:35.560+08:00\",\"user_id\":\"user_id\",\"user_name\":\"fasdf\"}";

        Comment comment =
                new ObjectMapper().readValue(json, Comment.class);
        assertEquals("123", comment.getId());
    }

}
