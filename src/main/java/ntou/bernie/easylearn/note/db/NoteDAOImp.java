package ntou.bernie.easylearn.note.db;

import ntou.bernie.easylearn.note.core.Note;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.Collections;
import java.util.List;

/**
 * Created by bernie on 2016/3/23.
 */
public class NoteDAOImp extends BasicDAO<Note, ObjectId> implements NoteDAO {

    public NoteDAOImp(Class<Note> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public List<Note> getNotesByVersionId(String versionId) {
        if (versionId == null)
            return Collections.emptyList();
        return createQuery().field("versionId").equal(versionId).asList();
    }

    @Override
    public void sync(List<Note> notes) {

    }
}
