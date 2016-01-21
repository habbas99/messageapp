package com.messageapp.utils;

import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonConvertor {
private final static Logger LOGGER = Logger.getLogger(GsonConvertor.class.getName()); 
	
	public static Gson INSTANCE;
	
	public GsonConvertor() {
		// nothing
	}
	
	public static void init() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ObjectId.class, new ObjectIdSerializer());
		gsonBuilder.registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer()); 
		INSTANCE = gsonBuilder.create();
		
		LOGGER.info("Gson convertor initialized");
	}
	
}
