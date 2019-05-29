package com.wm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.wm")
@SpringBootApplication
public class TicketApplication {
 

	public static void main(String[] args) {
		SpringApplication.run(TicketApplication.class, args);
	}
 
}
