package com.messageapp.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.messageapp.model.user.User;
import com.messageapp.model.user.UserExistsException;
import com.messageapp.utils.GsonConvertor;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(RegistrationServlet.class.getName());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		if(type.equals("activate")) {
			String id = request.getParameter("id");
			User user = User.getUserById(id);
			if(user.isPending()) {
				user.activate();
			}
			
			response.sendRedirect("/authenticate.jsp?type=login");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String content = request.getReader().readLine();
		
		try {
			User user = GsonConvertor.INSTANCE.fromJson(content, User.class);
			User originalUser = User.getUser(user.getEmail());
			
			if(type.equals("invite")) {
				user.selfInvite(originalUser);
			}
			else if(type.equals("remind")) {
				originalUser.sendActivationEmail();
			}
			else {
				LOGGER.severe("Invalid user request type " + type);
				throw new ServletException("Invalid user request type " + type);
			}
		}
		catch(UserExistsException e) {
			LOGGER.log(Level.WARNING ,"User is already registered", e);
			response.setStatus(UserExistsException.USER_EXISTS_CODE);
		}
		catch(Exception e) {
			LOGGER.log(Level.SEVERE ,"Failed to process request " + type, e);
			throw new ServletException("Failed to process request " + type);
		}
		
	}

}
