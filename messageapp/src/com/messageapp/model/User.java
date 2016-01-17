package com.messageapp.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.messageapp.db.MorphiaDatastore;

@Entity
public class User {
	@Id private ObjectId id;
	private String firstName = "1";
	private String lastName;
	
	public User() {
		
	}
	
	public static String getName() {
		MorphiaDatastore.INSTANCE.save(new User());
		return "Mr. Test Aaa";
	}
}