package com.messageapp.model.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.messageapp.db.AppDatastore;
import com.messageapp.model.user.User;

@Entity
public class Message {
	@Id
	private ObjectId id;
	
	private String subject;
	private String content;
	private boolean isPalindrome;
	
	private ObjectId senderID;
	private String senderEmail;
	private List<ObjectId> recipientIDs;
	
	private Date createdDate;
	private Date updatedDate;
	
	public Message() {
		// nothing
	}
	
	public ObjectId getID() {
		return id;
	}
	
	public void create(User sender) throws IOException {
		// TODO: check if the content of the message is a palindrome
		senderID = sender.getID();
		senderEmail = sender.getEmail();
		createdDate = new Date();
		save();
	}
	
	private void save() {
		updatedDate = new Date();
		AppDatastore.INSTANCE.save(this);
	}
	
	public static Message getMessageById(String id) {
		return AppDatastore.INSTANCE.createQuery(Message.class).field("_id").equal(new ObjectId(id)).get();
	}
	
	public static List<Message> getSentMessages(ObjectId senderID) {
		return AppDatastore.INSTANCE.createQuery(Message.class).field("senderID").equal(senderID).asList();
	}
	
	public static List<Message> getReceivedMessages(ObjectId recipientID) {
		List<ObjectId> recipientIDs = new ArrayList<ObjectId>();
		recipientIDs.add(recipientID);
		return AppDatastore.INSTANCE.createQuery(Message.class).field("recipientIDs").in(recipientIDs).asList();
	}
}
