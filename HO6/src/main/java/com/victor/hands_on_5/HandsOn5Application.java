package com.victor.hands_on_5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HandsOn5Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HandsOn5Application.class, args);
    }
    private static final Logger logger
            = LoggerFactory.getLogger(HandsOn5Application.class);

    @Override
    public void run(String... args) throws Exception {
        System.out.println("WELCOME TO THE WORLD OF SPRING");
        logger.info("Logger configuration from {} Logback", HandsOn5Application.class.getSimpleName());
    }
}
