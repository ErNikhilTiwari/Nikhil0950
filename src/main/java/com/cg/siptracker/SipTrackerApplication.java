package com.cg.siptracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SipTrackerApplication {

	public static void main(String[] args) {

		SpringApplication.run(SipTrackerApplication.class, args);
	}

}
