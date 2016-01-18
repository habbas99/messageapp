package com.messageapp.utils;

import java.util.logging.Logger;

import com.google.gson.Gson;

public class GsonConvertor {
private final static Logger LOGGER = Logger.getLogger(GsonConvertor.class.getName()); 
	
	public static Gson INSTANCE;
	
	public GsonConvertor() {
		// nothing
	}
	
	public static void init() {
		INSTANCE = new Gson();
		LOGGER.info("Gson convertor initialized");
	}
	
}
