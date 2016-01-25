package com.messageapp.model.user;

import javax.servlet.ServletException;

public class UserExistsException extends ServletException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int USER_EXISTS_CODE = 530;
	
	public UserExistsException(User user) {
		super("User " + user.getEmail() + "already exists");
	}
}
