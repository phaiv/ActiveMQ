package com.phaivv.activemq;

import javax.jms.DeliveryMode;
import javax.jms.Destination;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendMessage {
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	
	public void sendMessage(String msg) throws Exception {
		MessageProducer producer = null;
		Session session=null;
        try {
            // Create a session.
        	session = connectionFactory.createSession();
            
            // Create a queue named "MyQueue".
            final Destination producerDestination = session.createQueue("phaivv");
            
            // Create a producer from the session to the queue.
            producer = session.createProducer(producerDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        
            // Create a message.
            
            final TextMessage producerMessage = session.createTextMessage(msg);
        
            // Send the message.
            producer.send(producerMessage);
            System.out.println("Message sent.");
           
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			close(producer);
			close(session);
		}
       
    
   
    
        // Clean up the producer.
      
        
    }
	
	
	private void close(MessageProducer producer){
		try {
			if(producer!=null)producer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void close(Session session){
		try {
			if(session!=null)session.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
