package com.messageapp.model.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

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
	private List<String> recipientEmails;
	
	private Date createdDate;
	private Date updatedDate;
	
	public Message() {
		// nothing
	}
	
	public ObjectId getID() {
		return id;
	}
	
	public void create(User sender) throws IOException {
		isPalindrome = isPalindrome();
		senderID = sender.getID();
		senderEmail = sender.getEmail();
		createdDate = new Date();
		save();
	}
	
	private boolean isPalindrome() {
		boolean isPalindrome = true;
		
		int i = 0, j = content.length() - 1;
		while(true) {
			char left = content.charAt(i);
			char right = content.charAt(j);
			
			boolean isLeftValid = Character.isLetterOrDigit(left);
			boolean isRightValid = Character.isLetterOrDigit(right);
			
			if(isLeftValid && isRightValid) {
				if(Character.toLowerCase(left) == Character.toLowerCase(right)) {
					i++;
					j--;
				}
				else {
					isPalindrome = false;
					break;
				}
			}
			else if(isLeftValid) {
				j--;
			}
			else if(isRightValid) {
				i++;
			}
			else {
				i++;
				j--;
			}
			
			if(i >= j) {
				break;
			}
		}
		
		return isPalindrome;
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
	
	public static void deleteSentMessages(List<ObjectId> messageIDs, ObjectId recipientID) {
		UpdateOperations<Message> ops = AppDatastore.INSTANCE.createUpdateOperations(Message.class).unset("senderID");
		AppDatastore.INSTANCE.update(getMessages(messageIDs), ops); 
	}
	
	public static void deleteReceivedMessages(List<ObjectId> messageIDs, ObjectId recipientID) {
		UpdateOperations<Message> ops = AppDatastore.INSTANCE.createUpdateOperations(Message.class).removeAll("recipientIDs", recipientID);
		AppDatastore.INSTANCE.update(getMessages(messageIDs), ops); 
	}
	
	private static Query<Message> getMessages(List<ObjectId> messageIDs) {
		return AppDatastore.INSTANCE.createQuery(Message.class).field("_id").in(messageIDs);
	}
}