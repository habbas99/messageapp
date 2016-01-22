package com.messageapp.utils;

import org.bson.types.ObjectId;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ObjectIdSerializer implements JsonSerializer<ObjectId> {
	  public JsonElement serialize(ObjectId id, Type typeOfSrc, JsonSerializationContext context) {
	    return new JsonPrimitive(id.toHexString());
	  }
}  