package ntou.bernie.easylearn.pack.db;

import ntou.bernie.easylearn.pack.core.Pack;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

import java.util.List;

/**
 * Created by bernie on 2016/2/26.
 */
public interface PackDAO extends DAO<Pack, ObjectId> {
    boolean isExist(String packId);

    void sync(Pack pack);

    Pack getPackById(String packId);

    List<Pack> getPacksById(List<String> packs);
}
