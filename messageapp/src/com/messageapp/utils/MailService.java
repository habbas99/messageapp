package com.messageapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.text.StrSubstitutor;

public class MailService {
	private final static Logger LOGGER = Logger.getLogger(MailService.class.getName());
	
	private static String EMAIL_TEMPLATE_PATH = "/WEB-INF/templates/email/";
	private static String SUBJECT_ATTRIBUTE_PREFIX = "EMAIL_SUBJECT_";
	private static String FROM_EMAIL_ATTRIBUTE = "FROM_EMAIL";
	private static String FROM_EMAIL_PASSWORD_ATTRIBUTE = "FROM_EMAIL_PASSWORD";
	private static String PROTOCOL_ATTRIBUTE = "PROTOCOL";
	private static String HOST_ATTRIBUTE = "HOST";
	private static String SMTP_HOST_ATTRIBUTE = "SMTP_HOST";
	private static String SMTP_PORT_ATTRIBUTE = "SMTP_PORT";
	private static String REGISTRATION_LINK_ATTRIBUTE = "RELATIVE_REGISTRATION_LINK";
	
	public static MailService INSTANCE;
	private ServletContext context;
	private String protocol;
	private String host;
	private String smtpHost;
	private String smtpPort;
	private String from;
	private String password;
	private String registrationLink;
	
	
	public MailService(ServletContext context) {
		this.context = context;
	}
	
	public void sendEmail(String type, String to, Map<String, String> params) throws IOException {
		addDefaultParams(params);
		String subject = getSubject(type);
		String content = getContent(type, params);

		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", smtpHost);
		properties.setProperty("mail.smtp.port", smtpPort);

		
		Authenticator authenticator = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
            }
    	};
	        
		  
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties, authenticator);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setContent(content, "text/html");

			// Send message
			Transport.send(message);
			LOGGER.info("Message successfully sent to user " + to);
		}
		catch (MessagingException e) {
			LOGGER.log(Level.SEVERE ,"Failed to send email", e);
		}
	}
	
	private String getContent(String type, Map<String, String> params) {
		InputStream stream = context.getResourceAsStream(EMAIL_TEMPLATE_PATH + type + ".html");
		
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		
		try {

			br = new BufferedReader(new InputStreamReader(stream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return replaceTokens(sb.toString(), params);
	}
	
	private String getSubject(String type) {
		String subject = SUBJECT_ATTRIBUTE_PREFIX + type.toUpperCase();
		return context.getInitParameter(subject);
	}
	
	private String replaceTokens(String content, Map<String, String> params) {
		StrSubstitutor sub = new StrSubstitutor(params);
		return sub.replace(content);
	}
	
	private void addDefaultParams(Map<String, String> params) {
		params.put(PROTOCOL_ATTRIBUTE, protocol);
		params.put(HOST_ATTRIBUTE, host);
		params.put(REGISTRATION_LINK_ATTRIBUTE, registrationLink);
	}
	
	public static void init(ServletContext context) {
		INSTANCE = new MailService(context);
		INSTANCE.from = context.getInitParameter(FROM_EMAIL_ATTRIBUTE);
		INSTANCE.password = context.getInitParameter(FROM_EMAIL_PASSWORD_ATTRIBUTE);
		INSTANCE.protocol = context.getInitParameter(PROTOCOL_ATTRIBUTE);
		INSTANCE.host = context.getInitParameter(HOST_ATTRIBUTE);
		INSTANCE.smtpHost = context.getInitParameter(SMTP_HOST_ATTRIBUTE);
		INSTANCE.smtpPort = context.getInitParameter(SMTP_PORT_ATTRIBUTE);
		INSTANCE.registrationLink = context.getInitParameter(REGISTRATION_LINK_ATTRIBUTE);
		
	}
}
