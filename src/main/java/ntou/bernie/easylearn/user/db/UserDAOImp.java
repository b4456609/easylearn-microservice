package ntou.bernie.easylearn.user.db;

import ntou.bernie.easylearn.user.core.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

/**
 * Created by bernie on 2016/2/20.
 */
public class UserDAOImp extends BasicDAO<User, ObjectId> implements UserDAO {

    public UserDAOImp(Class<User> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public User getByUserId(String userId) {
        return createQuery().field("id").equal(userId).get();
    }

    @Override
    public boolean isExist(String userId) {
        User user = getByUserId(userId);
        if(user == null)
            return false;
        else
            return true;
    }


}
