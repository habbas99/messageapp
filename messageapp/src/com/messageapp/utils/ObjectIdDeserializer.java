package com.messageapp.utils;

import org.bson.types.ObjectId;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class ObjectIdDeserializer implements JsonDeserializer<ObjectId> {
	  public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		    ObjectId id = null;
		    
		    try {
			  id = new ObjectId(json.getAsJsonPrimitive().getAsString());
			  return id;
		    }
		    catch (Exception e) {
				e.printStackTrace();
		    }
		    
		    return id;
       }
} 