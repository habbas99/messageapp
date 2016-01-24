package com.messageapp.web;

import java.util.List;

import org.bson.types.ObjectId;

import com.messageapp.model.message.Message;

public class MessageRequest {
	protected Message message;
	protected List<ObjectId> selectedMessageIDs; 
}
