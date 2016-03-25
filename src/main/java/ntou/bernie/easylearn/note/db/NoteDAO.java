package ntou.bernie.easylearn.note.db;

import ntou.bernie.easylearn.note.core.Comment;
import ntou.bernie.easylearn.note.core.Note;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

import java.util.List;

/**
 * Created by bernie on 2016/3/23.
 */
public interface NoteDAO extends DAO<Note, ObjectId> {
    void sync(List<Note> notes);
    public List<Note> getNotesByVersionId(String versionId);
    public List<Comment> getCommentsByNoteId(String noteId);
    public void addComment(String noteId, Comment comment);
}
