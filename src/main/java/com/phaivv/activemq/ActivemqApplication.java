package com.phaivv.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.phaivv")
public class ActivemqApplication {
 
	public static void main(String[] args) { 
		SpringApplication.run(ActivemqApplication.class, args);
	}

}
