package com.project1.ms_yanki_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsYankiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsYankiServiceApplication.class, args);
    }

}
