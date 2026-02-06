package com.nimblix.SchoolPEPProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.EnableAsync;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableAsync
public class SchoolPepProjectApplication {

	public static void main(String[] args) {

        SpringApplication.run(SchoolPepProjectApplication.class, args);

        LocalDate localDate=LocalDate.now();
        System.out.println("Current Date: "+localDate);

        LocalDateTime localDateTime=LocalDateTime.now();
        System.out.println("Current Date: "+localDateTime);

//
//        DateTimeFormatter dateFormat= DateTimeFormatter.ofPattern("dd-mm-yyyy");
//        String formattedDate=localDate.format(dateFormat);
//        System.out.println(formattedDate);
//

        Month month=localDateTime.getMonth();
        DayOfWeek day=localDateTime.getDayOfWeek();
        System.out.println("Month: "+month);
        System.out.println("Day: "+day);

    }

}
