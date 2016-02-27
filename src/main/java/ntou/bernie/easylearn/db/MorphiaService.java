package ntou.bernie.easylearn.db;

import ntou.bernie.easylearn.DatabaseConfiguration;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorphiaService {
    private Morphia morphia;
    private Datastore datastore;

    public MorphiaService(DatabaseConfiguration databaseConfiguration) throws Exception {
        this.morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreEmpties(true);
        String databaseName = "easylearn";
        this.datastore = morphia.createDatastore(databaseConfiguration.getMongoClient(), databaseName);
        datastore.ensureIndexes();
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public void setMorphia(Morphia morphia) {
        this.morphia = morphia;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }
}
