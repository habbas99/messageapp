package com.messageapp.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MorphiaDatastore {
	public static Datastore INSTANCE;
	
	public MorphiaDatastore() {
		// nothing
	}
	
	public static void init(String connectionString, String dbName) {
		MongoClientURI connectionUri = new MongoClientURI(connectionString);
		MongoClient mongoClient = new MongoClient(connectionUri);
		
		INSTANCE = new Morphia().createDatastore(mongoClient, dbName);
	}
}