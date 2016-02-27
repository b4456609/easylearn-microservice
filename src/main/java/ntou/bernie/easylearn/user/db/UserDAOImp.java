package ntou.bernie.easylearn.user.db;

import ntou.bernie.easylearn.user.core.User;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

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
        if (user == null)
            return false;
        else
            return true;
    }

    @Override
    public boolean isConflict(User user) {
        User dbUser = getByUserId(user.getId());
        int dbVersion = dbUser.getSetting().getVersion();
        boolean isConflict = user.getSetting().isConflict(dbVersion);
        return isConflict;
    }

    @Override
    public void sync(User user) {
        Query<User> userQuery = createQuery().field("id").equal(user.getId());
        UpdateOperations<User> updateOptions = createUpdateOperations();
        updateOptions.set("lastUpTime", new DateTime().getMillis());
        updateOptions.set("setting", user.getSetting());
        updateOptions.set("folder", user.getFolder());
        update(userQuery, updateOptions);
    }
}
