package com.messageapp.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.messageapp.db.AppDatastore;
import com.messageapp.utils.GsonConvertor;
import com.messageapp.utils.MailService;

/**
 * Application Lifecycle Listener implementation class AppContextListener
 *
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public AppContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	// + uri + ":27018," + uri + ":27019";
    	
    	ServletContext context = servletContextEvent.getServletContext();
    	String connectionUrl = context.getInitParameter("DB_URL");
    	String dbName = context.getInitParameter("DB_NAME");
		
    	AppDatastore.init(connectionUrl, dbName);
		GsonConvertor.init();
		MailService.init(context);
    }
	
}
