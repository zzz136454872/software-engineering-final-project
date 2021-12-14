package com.softwareengineering.userloginregister.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table
@AllArgsConstructor
public class User {
    @Id
    private Long id;
    private String role;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String jobNum;
    private String studentClass;
    private String title;
    private String major;
    private String email;
    private String resume;

    public User() {}
}