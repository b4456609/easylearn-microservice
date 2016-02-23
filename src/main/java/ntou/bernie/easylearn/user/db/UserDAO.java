package ntou.bernie.easylearn.user.db;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;

import ntou.bernie.easylearn.user.core.User;
import org.mongodb.morphia.dao.DAO;

import java.util.List;


public interface UserDAO extends DAO<User, ObjectId> {

	public User getByUserId(String userId);
	public boolean isExist(String userId);
	public boolean isConflict(User user);

}
