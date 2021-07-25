package com.phaivv.activemq;


import javax.jms.Destination;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageReceived {
	
	
	 
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	
	public String received(){
		MessageConsumer consumer= null; 
		Session session = null;
		String result = "";
        try {


        	session = connectionFactory.createSession();
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("phaivv");
            
            // Create a MessageConsumer from the Session to the Topic or Queue
            consumer = session.createConsumer(destination);
//            Message reply = null;
//            int receiveTime = 0;
//			while (receiveTime / 2 <= 60*1000) {
//				try {
//					Thread.sleep(500);
//					reply = consumer.receiveNoWait();
//					if (reply != null)
//						break;
//					receiveTime++;
//				} catch (Exception e) {	 
////					lmr.error(e.toString(), LOCATION + "#RECEIVE-MSG");
//				}
//			}
            Message reply = consumer.receive(10000);
            
			if (reply != null) {
				result = ((TextMessage) reply).getText();
				
				System.out.println("result: " + result);
			} else {
//				istimeout = true;
				 result="No record";
			}
			

          
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
            result=e.toString();
        }finally {
        	close(consumer);
        	close(session);
		}
        return result;
	}
	
	private void close(MessageConsumer consumer){
		try {
			if(consumer!=null)consumer.close();
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
