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
import com.messageapp.utils.GsonConvertor;
import com.messageapp.utils.MailService;

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
			user.activate();
			
			response.sendRedirect("/login.html");
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
			if(type.equals("invite")) {
				user.invite();
			}
		}
		catch(Exception e) {
			LOGGER.log(Level.SEVERE ,"Failed to process request " + type, e);
			throw new ServletException("Failed to process request " + type);
		}
		
	}

}
