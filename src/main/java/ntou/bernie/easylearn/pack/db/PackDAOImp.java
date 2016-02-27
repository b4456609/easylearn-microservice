package ntou.bernie.easylearn.pack.db;

import ntou.bernie.easylearn.pack.core.Pack;
import ntou.bernie.easylearn.pack.core.Version;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Iterator;
import java.util.List;

/**
 * Created by bernie on 2016/2/26.
 */
public class PackDAOImp extends BasicDAO<Pack,ObjectId> implements PackDAO{
    public PackDAOImp(Class<Pack> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public boolean isExist(String packId) {
        return exists("id",packId);
    }

    @Override
    public void sync(Pack pack) {
        if(!exists("id",pack.getId())){
            save(pack);
            return;
        }

        Pack dbPack = getPackById(pack.getId());

        //delete or sync
        List<Version> versions = pack.getVersion();
        Iterator<Version> iterator = versions.iterator();
        while (iterator.hasNext()) {
            Version version = iterator.next();
            if (version.getModified().equals("delete"))
                //delete version
                iterator.remove();
            else if (version.getModified().equals("false")) {
                //not modified
                continue;
            } else {
                //sync version
                version.sync(dbPack.getVersionById(version.getId()));
            }
        }

        UpdateOperations<Pack> updateOp = createUpdateOperations()
                .set("isPublic", pack.getIsPublic())
                .set("version", pack.getVersion());
        update(createQuery().field("id").equal(pack.getId()),updateOp);
    }

    @Override
    public Pack getPackById(String packId) {
        return findOne("id",packId);
    }

    @Override
    public List<Pack> getPacksById(List<String> packs) {
        return createQuery().field("id").in(packs).asList();
    }
}
