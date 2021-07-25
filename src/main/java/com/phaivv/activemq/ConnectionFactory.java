package com.phaivv.activemq;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
//import javax.jms.MessageProducer;
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
	private Object LOCK_CONNECTION = new Object();
	private boolean isConnectionOK = true;
	private long retryTime;
	
	
	
	public long getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(long retryTime) {
		this.retryTime = retryTime;
	}

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
					public void onException(JMSException jmsEx) {	
						//todo reconnect
						System.out.println("reconnect connection cause by: " +jmsEx.toString());
						startListener();
					}
				});
		} catch (Exception e) {
			 throw new RuntimeException(e.toString());
		}
		
	}
	
	public Session createSession() {
		Session session = null;
		if (connection != null) {
			try {
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			} catch (JMSException e) {
				 e.printStackTrace();
				 startListener();
			}
		}
		return session;
	}
	
	public void retryConnection() {
		System.out.println("Do retryConnection........");
		isConnectionOK = false;
		while (true) {
			if (isConnectionOK == false) {
				try {
					close(connection);
					Thread.sleep(3000);
				    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		            connectionFactory.setUserName(user);
		        	connectionFactory.setPassword(pw);
					this.connection = connectionFactory.createConnection();
					this.connection.start();
					isConnectionOK = true;// alert ok
					System.out.println("##################### RETRY CONNECTION-DONE ################################");
					break;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else
				break;
		}
	}
	
	private void startListener() {
		System.out.println("startListener: " + isConnectionOK);
		if (isConnectionOK) {
			if (System.currentTimeMillis() - retryTime <= 3 * 60 * 1000)
				return;
			synchronized (LOCK_CONNECTION) {
				if (isConnectionOK) {
					try {
						JmsConnListener listener = new JmsConnListener(this);
						Thread thread = new Thread(listener);
						thread.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	
	private void close(Connection connection){
		try {
			if(connection!=null)connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
