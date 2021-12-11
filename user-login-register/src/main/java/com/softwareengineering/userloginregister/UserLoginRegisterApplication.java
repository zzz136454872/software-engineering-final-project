package com.softwareengineering.userloginregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserLoginRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserLoginRegisterApplication.class, args);
    }

}
