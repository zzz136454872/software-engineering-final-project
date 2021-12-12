package com.softwareengineering.userconsume.controller;

import com.softwareengineering.userconsume.pojo.User;
import com.softwareengineering.userconsume.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("consume")
public class ConsumeController {
    @Autowired
    private UserService userService;
    @GetMapping
    public List<User> consume(@RequestParam("ids") List<Long> ids, HttpServletResponse response, @CookieValue(value="mycookie", defaultValue="") String myOldCookie) {
        response.addCookie(new Cookie("mycookie","test"));
        response.addCookie(new Cookie("mycookie1","test1"));
        if (!myOldCookie.equals("")) {
            response.addCookie(new Cookie("lastcookie",myOldCookie));
        }
        return this.userService.queryUserByIds(ids);
    }
}