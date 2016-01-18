package com.messageapp.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class AppDatastore {
	private final static Logger LOGGER = Logger.getLogger(AppDatastore.class.getName());
	
	public static Datastore INSTANCE;
	
	public AppDatastore() {
		// nothing
	}
	
	public static void init(String connectionUrl, String dbName) {
		try {
			MongoClientURI connectionUri = new MongoClientURI(connectionUrl);
			MongoClient mongoClient = new MongoClient(connectionUri);
			
			INSTANCE = new Morphia().createDatastore(mongoClient, dbName);
			
			LOGGER.info("Morphia Datastore initialized");
		}
		catch(Exception e) {
			LOGGER.log(Level.SEVERE ,"Failed to initialize Morphia Datastore", e);
		}
	}
}