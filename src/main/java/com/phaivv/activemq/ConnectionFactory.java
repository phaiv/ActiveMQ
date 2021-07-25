package com.phaivv.activemq;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName="singleton")
public class ConnectionFactory {
	
	@Value("${activeMqUser}")
	private String user;
	@Value("${activeMqPw}")
	private String pw;
	@Value("${activeMqUrl}")
	private String url;
	
	
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private Connection connection = null;
	
	@PostConstruct
	public void init(){
		try {
			 System.out.println("init connectionn");
			 ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
	            connectionFactory.setUserName(user);
	        	connectionFactory.setPassword(pw);
	            // Create a Connection
	            connection = connectionFactory.createConnection();
	            connection.start();
	            
	            connection.setExceptionListener(new ExceptionListener() {
					public void onException(JMSException paramJMSException) {	
						//todo reconnect
						 
					}
				});
	            System.out.println("connection: " + connection);
		} catch (Exception e) {
			//lmr.error("Cannot int JMS activeMQ ", LOCATION);
			 throw new RuntimeException(e.toString());
		}
		
	}
	
	public Session createSession() {
		Session session = null;
		 System.out.println("createSession: " + connection);
		if (connection != null) {
			try {
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
			} catch (JMSException e) {
				 e.printStackTrace();
			}
		}
		return session;
	}

}
