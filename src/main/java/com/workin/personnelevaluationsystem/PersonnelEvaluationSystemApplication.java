package com.workin.personnelevaluationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Import Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import BCryptPasswordEncoder

@SpringBootApplication
public class PersonnelEvaluationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonnelEvaluationSystemApplication.class, args);
    }

    @Bean // Makes BCryptPasswordEncoder available for injection
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}