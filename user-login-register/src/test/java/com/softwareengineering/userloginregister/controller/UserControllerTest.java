package com.softwareengineering.userloginregister.controller;

import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("UserController Test")
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
class UserControllerTest {
    @Resource
    UserController userController;

    @Test
    void signinSuccessTest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/user/signin")
                        .param("username","student1")
                        .param("password","509e87a6c45ee0a3c657bf946dd6dc43d7e5502143be195280f279002e70f7d9"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\":\"student1\"}"))
                .andExpect(cookie().exists("token"));
    }

    @Test
    void signinFailTest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/user/signin")
                        .param("username","student1")
                        .param("password","509e87a6c45ee0a3c657bf946dd6dc45d7e5502143be195280f279002e70f7d9"))
                .andExpect(status().is4xxClientError());
    }

}