package ntou.bernie.easylearn.user.db;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;

import ntou.bernie.easylearn.user.core.User;


public class UserDAO extends BasicDAO<User, ObjectId> {

	/**
	 * @param entityClass
	 * @param ds
	 */
	public UserDAO(Class<User> entityClass, Datastore ds) {
		super(entityClass, ds);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param entityClass
	 * @param mongoClient
	 * @param morphia
	 * @param dbName
	 */
	public UserDAO(Class<User> entityClass, MongoClient mongoClient, Morphia morphia, String dbName) {
		super(entityClass, mongoClient, morphia, dbName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ds
	 */
	public UserDAO(Datastore ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param mongoClient
	 * @param morphia
	 * @param dbName
	 */
	public UserDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
		// TODO Auto-generated constructor stub
	}

}
