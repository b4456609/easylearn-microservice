package ntou.bernie.easylearn.note.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class NoteTest {

	@Test
	public void testNoteStyle() throws JsonProcessingException {
		List<Comment> comment = new ArrayList<Comment>();
		comment.add(new Comment("id", "content", "create_timej", "fasdfID", "name"));
		comment.add(new Comment("id", "content", "create_timej", "fasdfID", "name"));
		Note note = new Note("id", "content", "create_time", "user_id", "user_name", "version_id", comment);

		ObjectMapper mapper = new ObjectMapper()
				.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		String jsonInString = mapper.writeValueAsString(note);
		
		String json = "{\"id\":\"id\",\"content\":\"content\",\"create_time\":\"create_time\",\"user_id\":\"user_id\",\"user_name\":\"user_name\",\"version_id\":\"version_id\",\"comments\":[{\"id\":\"id\",\"content\":\"content\",\"create_time\":\"create_timej\",\"user_id\":\"fasdfID\",\"user_name\":\"name\"},{\"id\":\"id\",\"content\":\"content\",\"create_time\":\"create_timej\",\"user_id\":\"fasdfID\",\"user_name\":\"name\"}]}";

		assertEquals(json, jsonInString);
	}

	@Test
	public void tesDeStyle() throws IOException {
		String json = "{\"id\":\"id\",\"content\":\"content\",\"create_time\":\"create_time\",\"user_id\":\"user_id\",\"user_name\":\"user_name\",\"version_id\":\"version_id\",\"comments\":[{\"id\":\"id\",\"content\":\"content\",\"create_time\":\"create_timej\",\"user_id\":\"fasdfID\",\"user_name\":\"name\"},{\"id\":\"id\",\"content\":\"content\",\"create_time\":\"create_timej\",\"user_id\":\"fasdfID\",\"user_name\":\"name\"}]}";

		ObjectMapper mapper = new ObjectMapper()
				.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		Note note = mapper.readValue(json, Note.class);
		

		assertEquals("create_time", note.getCreateTime());
	}
}
