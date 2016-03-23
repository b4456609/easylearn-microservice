package ntou.bernie.easylearn.note.representation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ntou.bernie.easylearn.note.core.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by bernie on 2016/3/23.
 */
public class NotesDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotesDeserializer.class);

    public List<Note> deserializer(String json, ObjectMapper mapper) throws IOException {
        //get pack id
        ArrayNode jsonNode = (ArrayNode) mapper.readTree(json);
        List<Note> notesArray = new ArrayList<Note>();

        Iterator<JsonNode> nodeIterator = jsonNode.elements();
        while (nodeIterator.hasNext()) {
            Iterator<JsonNode> versionsNode = nodeIterator.next().get("version").elements();

            while (versionsNode.hasNext()) {
                JsonNode version = versionsNode.next();
                String versionId = version.get("id").asText();
                Iterator<JsonNode> notes = version.get("note").elements();
                while (notes.hasNext()) {
                    ObjectNode noteJsonNode = (ObjectNode) notes.next();
                    noteJsonNode.put("version_id", versionId);
                    Note note = mapper.readValue(noteJsonNode.toString(), Note.class);
                    notesArray.add(note);
                }
            }

            LOGGER.debug(notesArray.toString());

        }
        return notesArray;
    }


    private boolean isValid(Note note) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //json validation
        Set<ConstraintViolation<Note>> constraintViolations = validator.validate(note);
        for (ConstraintViolation<Note> constraintViolation : constraintViolations) {
            LOGGER.warn(constraintViolation.toString());
        }
        if (constraintViolations.size() > 0) {
            return false;
        }
        return true;
    }
}
