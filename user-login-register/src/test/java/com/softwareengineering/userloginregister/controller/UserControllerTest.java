package com.softwareengineering.userloginregister.controller;

import com.netflix.discovery.converters.Auto;
import com.softwareengineering.userloginregister.pojo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    User user = new User(0L,
            "学生",
            "student9",
            "student9",
            "刘同学",
            "男",
            "s654321",
            "三班",
            (String) null,
            "abcd",
            "99999@buaa.edu.cn",
            "Hello");

    Cookie adminCookie = new Cookie("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoi566h55CG5ZGYIiwiaWQiOjMsImV4cCI6MTYzOTgwNzEwOSwidXNlcm5hbWUiOiJhZG1pbmFkbWluMSJ9.i2l_NYOQqTEpfnPUe7XKxrW0i88xYoafVNxS2uH2_20");
    Cookie adminCookieError = new Cookie("token","wOSwidXNlcm5hbWUiOiJhZG1pbmFkbWluMSJ9.i2l_NYOQqTEpfnPUe7XKxrW0i88xYoafVNxS2uH2_20");
    Cookie studentCookie = new Cookie("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoi5a2m55SfIiwiaWQiOjEsImV4cCI6MTYzOTgwODAxNSwidXNlcm5hbWUiOiJzdHVkZW50MSJ9.wXLtajbElfR3xcEkAMfJ7UybtngXEOkGZH8RRrhi_cw");
    Cookie studentCookieError = new Cookie("token","eyJhbGciOiJIUzI1NiIsInVCJ9.eyJyb2xlIjoi5a2m55SfIiwiaWQiOjEsImV4cCI6MTYzOTgwODAxNSwidXNlcm5hbWUiOiJzdHVkZW50MSJ9.wXLtajbElfR3xcEkAMfJ7UybtngXEOkGZH8RRrhi_cw");

    /**
     * 接口返回成功
     * @param mvc
     * @throws Exception
     */
    @Test
    void createUserSuccessTest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/user")
                        .content(JSON.toJSONString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie)
                )
                .andExpect(status().isOk());
    }

    /**
     * token有误
     * @param mvc
     * @throws Exception
     */
    @Test
    void createUserFailTest1(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/user")
                        .content(user.toString())
                        .cookie(adminCookieError)
                )
                .andExpect(status().is4xxClientError());
    }

    /**
     * 身份不为管理员
     * @param mvc
     * @throws Exception
     */
    @Test
    void createUserFailTest2(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/user")
                        .content(user.toString())
                        .cookie(studentCookie)
                )
                .andExpect(status().is4xxClientError());
    }


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
                        .param("password","509e87a69002e70f7d9"))
                .andExpect(status().is4xxClientError());
    }

}