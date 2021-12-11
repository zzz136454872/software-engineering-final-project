package com.softwareengineering.userloginregister.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.softwareengineering.userloginregister.pojo.User;
import com.softwareengineering.userloginregister.service.UserService;

import javax.ws.rs.POST;
import java.util.Map;


@RestController
@RequestMapping("userlogin")
public class UserController {
    @Autowired
    private UserService userService;

    //register
    @PostMapping("/register")
    public int register(@RequestBody Map<String, Object> data) {
        int id=(Integer)data.get("id");
        String role=(String)data.get("role");
        String username=(String)data.get("username");
        String password=(String)data.get("password");
        String name=(String)data.get("name");
        String gender=(String)data.get("gender");
        String jobNum=(String)data.get("jobNum");
        String studentClass=(String)data.get("studentClass");
        String title=(String)data.get("title");
        String major=(String)data.get("major");
        String email=(String)data.get("email");
        String resume=(String)data.get("resume");
        User user= new User(id, role, username, password, name, gender, jobNum, studentClass, title, major, email, resume);

        return this.userService.register(user);
    }

    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id) {
        System.out.print(id);
        return this.userService.queryById(id);
    }
}