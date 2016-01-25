package com.messageapp.model.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

import com.messageapp.db.AppDatastore;
import com.messageapp.utils.MailService;

@Entity
public class User {
	public static enum State {
		PENDING, ACTIVE
	}
	
	public static enum Role {
		USER, ADMIN
	}
	
	private static String GET_USERS_RETRIEVED_FIELDS = "email";
	
	@Id
	private ObjectId id;
	
	private String firstName;
	private String lastName;
	private String email;
	
	private String username;
	@Transient
	private String password;
	private String secret;
	
	private State state;
	private Role role;
	
	private Date createdDate;
	private Date updatedDate;
	
	
	public User() {
		
	}
	
	public ObjectId getID() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean isPending() {
		return state.equals(State.PENDING);
	}
	
	
	public void invite(User actor) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ACTOR_NAME", actor.email);
		params.put("REGISTRATION_LINK", actor.email);
		
		MailService.INSTANCE.sendEmail("invite", email, params);
	}
	
	public void selfInvite(User originalUser) throws UserExistsException, IOException {
		if(originalUser == null) {
			state = State.PENDING;
			secret = DigestUtils.md5Hex(password);
			createdDate = new Date();
			save();
			
			sendActivationEmail();
		}
		else {
			throw new UserExistsException(originalUser);
		}
	}
	
	public void sendActivationEmail() throws IOException {
		MailService.INSTANCE.sendEmail("activate", email, getActivateEmailParams());
	}
	
	public void activate() throws IOException {
		role = Role.USER;
		state = State.ACTIVE;
		save();
		
		MailService.INSTANCE.sendEmail("welcome", email, getWelcomeEmailParams());
	}
	
	private Map<String, String> getWelcomeEmailParams() {
		Map<String, String> params = new HashMap<String, String>();
		return params;
	}
	
	private Map<String, String> getActivateEmailParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("RELATIVE_ACTIVATION_LINK", "register?type=activate&id=" + id.toHexString());
		return params;
	}
	
	private void save() {
		updatedDate = new Date();
		AppDatastore.INSTANCE.save(this);
	}
	
	public static User getUser(String email) {
		return AppDatastore.INSTANCE.createQuery(User.class).field("email").equal(email).get();
	}
	
	public static User findActiveUser(String email) {
		return AppDatastore.INSTANCE.createQuery(User.class).field("email").equal(email).field("state").equal( State.ACTIVE).get();
	}
	
	public static User getUserById(String id) {
		return User.getUserById(new ObjectId(id));
	}
	
	public static User getUserById(ObjectId id) {
		return AppDatastore.INSTANCE.createQuery(User.class).field("_id").equal(id).get();
	}
	
	public static User getUser(String username, String secret) {
		return AppDatastore.INSTANCE.createQuery(User.class).field("username").equal(username).field("secret").equal(secret).get();
	}
	
	public static List<User> getUsers() {
		return AppDatastore.INSTANCE.createQuery(User.class).retrievedFields(true, GET_USERS_RETRIEVED_FIELDS).asList();
	}
}