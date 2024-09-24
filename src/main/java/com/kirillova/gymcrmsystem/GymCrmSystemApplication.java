package com.kirillova.gymcrmsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GymCrmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymCrmSystemApplication.class, args);
    }
}
