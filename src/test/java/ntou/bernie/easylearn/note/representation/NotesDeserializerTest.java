package ntou.bernie.easylearn.note.representation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ntou.bernie.easylearn.note.core.Note;
import org.junit.Test;

import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;

/**
 * Created by bernie on 2016/3/23.
 */
public class NotesDeserializerTest {
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void testDeserializer() throws Exception {
        String json = fixture("pack/packWithNote.json");
        NotesDeserializer notesDeserializer = new NotesDeserializer();
        List<Note> notes = notesDeserializer.deserializer(json, mapper);
        Note note = notes.get(1);
        assertEquals(1439382017000L, note.getCreateTime());
        assertEquals("1009840175700426", note.getUserId());
        assertEquals("note1439382017021", note.getId());
        assertEquals("燒焦的", note.getContent());
        assertEquals("范振原", note.getUserName());
        assertEquals(2, note.getComment().size());
    }
}