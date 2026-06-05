package com.pranav.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
// This one annotation replaces all the manual setup you did in plain Spring (web.xml, dispatcher servlet etc.)
@SpringBootApplication
public class SmartExpenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartExpenseApplication.class, args);
        System.out.println("Smart Expense Tracker is running at http://localhost:8080");
    }
}
