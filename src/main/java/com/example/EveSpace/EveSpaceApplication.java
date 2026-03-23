package com.example.EveSpace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EveSpaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EveSpaceApplication.class, args);
    }
}
