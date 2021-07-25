package com.phaivv.activemq;

import org.springframework.beans.factory.annotation.Autowired;

public class JmsConnListener implements Runnable {
	
	@Autowired
	private ConnectionFactory conn;
	
    public JmsConnListener(ConnectionFactory conn) {
    	this.conn = conn;
    	conn.setRetryTime(System.currentTimeMillis());
	 
    }
		
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.conn.retryConnection();
		
	}

}
