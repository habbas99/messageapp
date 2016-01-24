package com.messageapp.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
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
			LOGGER.log(Level.SEVERE ,"Request failed", e);
			throw new ServletException("Request failed");
		}
	}
	
	private void handleContextRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		User user = SessionManager.get(request.getSession());
		if(user == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		String result = null;
		String context = request.getParameter("context");
		
		if(context.equals("user")) {
			result = handleUserRequest(request, user);
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
	
	private String handleUserRequest(HttpServletRequest request, User actor) throws ServletException, IOException {
		String result = null;
		
		String method = request.getMethod();
		String type = request.getParameter("type");
		
		if(method.equals("GET")) {
			if(type.equals("profile")) {
				result = GsonConvertor.INSTANCE.toJson(User.getUserById(actor.getID()));
			}
			else if(type.equals("find")) {
				String email = URLDecoder.decode(request.getParameter("email"), "UTF-8");
				result = GsonConvertor.INSTANCE.toJson(User.findActiveUser(email));
			}
			else if(type.equals("users")) {
				result = GsonConvertor.INSTANCE.toJson(User.getUsers());
			}
			else {
				LOGGER.severe("Invalid user request type " + type);
				throw new ServletException("Invalid user request type " + type);
			}
		}
		else if(method.equals("POST")) {
			String content = request.getReader().readLine();
			User user = GsonConvertor.INSTANCE.fromJson(content, User.class);
			if(type.equals("invite")) {
				user.invite(actor);
			}
			else {
				LOGGER.severe("Invalid user request type " + type);
				throw new ServletException("Invalid user request type " + type);
			}
		}
		else {
			throw new ServletException("User requests does not support method " + method);
		}
		
		
		return result;
	}
	
	private String handleMessageRequest(HttpServletRequest request, User actor) throws ServletException, IOException {
		String result = null;
		
		String method = request.getMethod();
		String type = request.getParameter("type");
		
		if(method.equals("GET")) {
			if(type.equals("open")) {
				String id = request.getParameter("id");
				result = GsonConvertor.INSTANCE.toJson(Message.getMessageById(id));
			}
			else if(type.equals("received")) {
				result = GsonConvertor.INSTANCE.toJson(Message.getReceivedMessages(actor.getID()));
			}
			else if(type.equals("sent")) {
				result = GsonConvertor.INSTANCE.toJson(Message.getSentMessages(actor.getID()));
			}
			else {
				LOGGER.severe("Invalid message request type " + type);
				throw new ServletException("Invalid message request type " + type);
			}
		}
		else if(method.equals("POST")) {
			String content = request.getReader().readLine();
			MessageRequest messageRequest = GsonConvertor.INSTANCE.fromJson(content, MessageRequest.class);
			
			if(type.equals("create")) {
				messageRequest.message.create(actor);
				result = GsonConvertor.INSTANCE.toJson(messageRequest.message);
			}
			else if(type.equals("delete")) {
				String mode = request.getParameter("mode");
				if(mode.equals("recipient")) {
					Message.deleteReceivedMessages(messageRequest.selectedMessageIDs, actor.getID());
				}
				else {
					Message.deleteSentMessages(messageRequest.selectedMessageIDs, actor.getID());
				}
				
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