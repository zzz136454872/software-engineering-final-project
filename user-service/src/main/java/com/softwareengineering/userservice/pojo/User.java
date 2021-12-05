package com.softwareengineering.userservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@Data
@Table
public class User {
    @Id
    private Long id;
    private String username;
    private String phone;
}