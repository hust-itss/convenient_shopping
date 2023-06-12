package com.example.convenientshoppingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ConvenientShoppingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConvenientShoppingAppApplication.class, args);
    }

}
