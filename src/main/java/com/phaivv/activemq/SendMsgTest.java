package com.phaivv.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/produce", method=RequestMethod.POST, produces={"application/json"}, consumes={"application/json"})
public class SendMsgTest {
	
	@Autowired
	private SendMessage sendMessage;
	@PostMapping("/send")
	public ResponseEntity<String> sendMessagex(@RequestBody String request) {
		  
		try {
			 
			sendMessage.sendMessage(request);
			 
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("send msg error", HttpStatus.OK);
		}
		return new ResponseEntity<>("sent msg", HttpStatus.OK);
	}
	
	@Autowired
	private MessageReceived msess;
	@PostMapping("/received")
	public ResponseEntity<String> receivedMsg() {
		 String rs = "";
		try {
			 
			rs = msess.received();	 
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("received: " + rs, HttpStatus.OK);
	}
}
