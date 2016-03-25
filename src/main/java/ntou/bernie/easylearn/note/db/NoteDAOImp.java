package ntou.bernie.easylearn.note.db;

import ntou.bernie.easylearn.note.core.Comment;
import ntou.bernie.easylearn.note.core.Note;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    public List<Comment> getCommentsByNoteId(String noteId) {
        return createQuery().field("noteId").equal(noteId).get().getComment();
    }

    @Override
    public void addComment(String noteId, Comment comment) {
        Query<Note> noteQuery = createQuery().field("noteId").equal(noteId);
        Note note = noteQuery.get();
        UpdateOperations<Note> updateOperations = createUpdateOperations()
                .set("comment", note.getComment().add(comment));
        update(noteQuery,updateOperations);
    }

    @Override
    public void sync(List<Note> notes) {
        for (Note note : notes){
            Query<Note> noteQuery = createQuery().field("noteId").equal(note.getId());
            UpdateOperations<Note> updateOperations = createUpdateOperations()
                    .set("comment", note.getComment());
            update(noteQuery,updateOperations);
        }
    }
}
