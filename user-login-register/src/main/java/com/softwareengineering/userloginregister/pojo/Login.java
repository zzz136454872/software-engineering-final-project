package com.softwareengineering.userloginregister.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Data
public class Login {
    private HttpStatus status;
    private String token;
    private User user;
    public Login() {}
}
