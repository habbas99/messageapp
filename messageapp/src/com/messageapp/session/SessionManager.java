package com.messageapp.session;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import com.messageapp.model.user.User;
import com.messageapp.utils.GsonConvertor;

public class SessionManager {
	private final static Logger LOGGER = Logger.getLogger(SessionManager.class.getName()); 
	private static String USER_SESSION_ATTRIBUTE_NAME = "messageapp.user";
	
	public static boolean login(HttpSession session, String username, String password) {
		String secret = DigestUtils.md5Hex(password);
		User user  = User.getUser(username, secret);
		
		if(user == null) {
			LOGGER.severe("User " + username + " not found");
			return false;
		}
		
		String content = GsonConvertor.INSTANCE.toJson(user);
		session.setAttribute(USER_SESSION_ATTRIBUTE_NAME, content);
		
		LOGGER.info("User " + username + " added to session " + session.getId());
		return true;
	}
	
	public static User get(HttpSession session) {
		String content = (String) session.getAttribute(USER_SESSION_ATTRIBUTE_NAME);
		
		if(content == null) {
			LOGGER.severe("No user exists in session " + session.getId());
			return null;
		}
		
		User user = GsonConvertor.INSTANCE.fromJson(content, User.class);
		LOGGER.info("Returning user " + user.getEmail() + " for session " + session.getId());
		
		return user;
	}
}
