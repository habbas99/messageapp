package com.messageapp.session;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import com.messageapp.model.user.User;
import com.messageapp.utils.GsonConvertor;

public class SessionManager {
	private final static Logger LOGGER = Logger.getLogger(SessionManager.class.getName()); 
	private static String USER_SESSION_ATTRIBUTE_NAME = "messageapp.user";
	private static String IS_AUTH_SESSION_ATTRIBUTE_NAME = "messageapp.isAuthenticated";
	
	public static boolean login(HttpSession session, String username, String password) {
		String secret = DigestUtils.md5Hex(password);
		User user  = User.getUser(username, secret);
		
		if(user == null) {
			LOGGER.severe("User " + username + " not found");
			return false;
		}
		
		String content = GsonConvertor.INSTANCE.toJson(user);
		session.setAttribute(USER_SESSION_ATTRIBUTE_NAME, content);
		session.setAttribute(IS_AUTH_SESSION_ATTRIBUTE_NAME, true);
		
		LOGGER.info("User " + username + " added to session " + session.getId());
		return true;
	}
	
	public static void logout(HttpSession session) {
		session.removeAttribute(USER_SESSION_ATTRIBUTE_NAME);
		session.removeAttribute(IS_AUTH_SESSION_ATTRIBUTE_NAME);
		session.invalidate();
		LOGGER.info("Logged out user from session " + session.getId());
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
