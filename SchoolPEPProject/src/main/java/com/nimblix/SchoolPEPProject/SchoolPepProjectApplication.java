package com.nimblix.SchoolPEPProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SchoolPepProjectApplication {

	public static void main(String[] args) {

        SpringApplication.run(SchoolPepProjectApplication.class, args);
	}

}
