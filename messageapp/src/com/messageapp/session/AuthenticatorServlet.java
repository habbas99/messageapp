package com.messageapp.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AuthenticatorServlet
 */
@WebServlet("/authenticator")
public class AuthenticatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthenticatorServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		
		if(type.equalsIgnoreCase("login")) {
			String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        
	        boolean isSuccess = SessionManager.login(request.getSession(), username, password);
	        if(isSuccess) {
	        	response.sendRedirect("/app/index.html");
	        }
	        else {
	        	response.sendRedirect("/loginerror.html");
	        }
		}
		else if(type.equalsIgnoreCase("logout")) {
			SessionManager.logout(request.getSession());
		}
		else {
			throw new ServletException("Unsupported type " + type);
		}
	}

}
