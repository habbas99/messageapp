package com.messageapp.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.messageapp.model.message.Message;
import com.messageapp.model.user.User;
import com.messageapp.session.SessionManager;
import com.messageapp.utils.GsonConvertor;

/**
 * Servlet implementation class MessageAppServlet
 */
@WebServlet("/message/data")
public class MessageAppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(MessageAppServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageAppServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			handleContextRequest(request, response);
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE ,"Failed to send email", e);
			throw new ServletException("Request failed");
		}
	}

	/**
	 * @throws ServletException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			handleContextRequest(request, response);
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE ,"Failed to send email", e);
			throw new ServletException("Request failed");
		}
	}
	
	private void handleContextRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		User user = SessionManager.get(request.getSession());
		if(user == null) {
			response.sendRedirect("/login.jsp");
			return;
		}
		
		String result = null;
		String context = request.getParameter("context");
		
		if(context.equals("user")) {
			result = handleUserRequest(request);
		}
		else if(context.equals("message")) {
			result = handleMessageRequest(request, user);
		}
		else {
			LOGGER.severe("Invalid context " + context);
			throw new ServletException("Invalid context " + context);
		}
		
		if(result != null) {
			response.getWriter().append(result);
		}
	}
	
	private String handleUserRequest(HttpServletRequest request) throws ServletException {
		String result = null;
		
		String method = request.getMethod();
		String type = request.getParameter("type");
		
		if(method.equals("GET")) {
			if(type.equals("users")) {
				result = GsonConvertor.INSTANCE.toJson(User.getUsers());
			}
			else {
				LOGGER.severe("Invalid user request type " + type);
				throw new ServletException("Invalid user request type " + type);
			}
		}
		else if(method.equals("POST")) {
			
		}
		else {
			throw new ServletException("User requests does not support method " + method);
		}
		
		
		return result;
	}
	
	private String handleMessageRequest(HttpServletRequest request, User user) throws ServletException, IOException {
		String result = null;
		
		String method = request.getMethod();
		String type = request.getParameter("type");
		
		if(method.equals("GET")) {
			if(type.equals("open")) {
				String id = request.getParameter("id");
				result = GsonConvertor.INSTANCE.toJson(Message.getMessageById(id));
			}
			else if(type.equals("received")) {
				result = GsonConvertor.INSTANCE.toJson(Message.getReceivedMessages(user.getID()));
			}
			else if(type.equals("sent")) {
				result = GsonConvertor.INSTANCE.toJson(Message.getSentMessages(user.getID()));
			}
			else {
				LOGGER.severe("Invalid message request type " + type);
				throw new ServletException("Invalid message request type " + type);
			}
		}
		else if(method.equals("POST")) {
			String content = request.getReader().readLine();
			Message message = GsonConvertor.INSTANCE.fromJson(content, Message.class);
			
			if(type.equals("create")) {
				message.create(user);
				result = GsonConvertor.INSTANCE.toJson(message);
			}
			else {
				LOGGER.severe("Invalid message request type " + type);
				throw new ServletException("Invalid message request type " + type);
			}
		}
		else {
			throw new ServletException("User requests does not support method " + method);
		}
		
		
		return result;
	}
	

}