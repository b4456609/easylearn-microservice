package ntou.bernie.easylearn.user.db;

import ntou.bernie.easylearn.user.core.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;


public interface UserDAO extends DAO<User, ObjectId> {

    public User getByUserId(String userId);

    public boolean isExist(String userId);

    public boolean isConflict(User user);

    public void sync(User user);
}
