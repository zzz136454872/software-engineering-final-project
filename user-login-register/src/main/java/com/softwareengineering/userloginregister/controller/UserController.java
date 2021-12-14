package com.softwareengineering.userloginregister.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.softwareengineering.userloginregister.pojo.User;
import com.softwareengineering.userloginregister.pojo.Login;
import com.softwareengineering.userloginregister.util.JwtUtil;
import com.softwareengineering.userloginregister.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    //@PostMapping("/update")
    public ResponseEntity<Long> createUser(@RequestBody User user) throws Exception {
        // do some magic!
        return new ResponseEntity<Long>(this.userService.createUser(user), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        // do some magic!
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<User>((User) null,HttpStatus.UNAUTHORIZED);
        }

        User user = this.userService.getUserById(userId);
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@RequestBody User user,@PathVariable("userId") Long userId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        // do some magic!
        log.info("updateUser");
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<User>((User) null,HttpStatus.UNAUTHORIZED);
        }

        this.userService.updateUserById(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable("userId") Long userId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        // do some magic!
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<User>((User) null,HttpStatus.UNAUTHORIZED);
        }

        this.userService.deleteUserById(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "id", required = false) List<Long> id_list, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        // do some magic!
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            System.out.println("gg");
            return new ResponseEntity<List<User>>((List<User>) null,HttpStatus.UNAUTHORIZED);
        }
        List<User> users = this.userService.getUserByIds(id_list);
        return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }

    @DeleteMapping("/list")
    public ResponseEntity deleteUsers(@RequestBody List<Long> id_list, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        // do some magic!
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<List<User>>((List<User>) null,HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtil.getRole(token).equals("管理员")){
            return new ResponseEntity<List<User>>((List<User>) null,HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(this.userService.deleteUserByIds(id_list));
    }

    @GetMapping("/aaa")
    public List<User> login_exm(@RequestParam("ids") List<Long> ids, HttpServletResponse response, @CookieValue(value="mycookie", defaultValue="") String myOldCookie) {

        response.addCookie(new Cookie("mycookie","test"));
        response.addCookie(new Cookie("mycookie1","test1"));
        if (!myOldCookie.equals("")) {
            response.addCookie(new Cookie("lastcookie",myOldCookie));
        }
        return this.userService.getUserByIds(ids);
    }






    @PostMapping("/update")
    public int update(HttpServletRequest request, HttpServletResponse response, @CookieValue(value="token", defaultValue="") String myCookie) {
        //String req = request.getQueryString();
        String id_str = request.getParameter("id");
        Long id = null;
        try {
            id = Long.parseLong(id_str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //int id = queryString.
        User user= new User();
        user.setId(id);
        user.setRole(request.getParameter("role"));
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setName(request.getParameter("name"));
        user.setGender(request.getParameter("gender"));
        user.setJobNum(request.getParameter("jobNum"));
        user.setStudentClass(request.getParameter("studentClass"));
        user.setTitle(request.getParameter("title"));
        user.setMajor(request.getParameter("major"));
        user.setEmail(request.getParameter("email"));
        user.setResume(request.getParameter("resume"));

        return this.userService.update(user, myCookie);
    }

    @GetMapping("/signin")
    public ResponseEntity<User> signin(@RequestParam(name = "username", required = true) String username,
                             @RequestParam(name = "password", required = true) String password,
                             HttpServletResponse response
    ) {
        User user= new User();
        user.setUsername(username);
        user.setPassword(password);

        Login res = this.userService.login(user);

        //System.out.println("inside "+res);
        if(res.getStatus().equals(HttpStatus.OK)) {
            response.addCookie(new Cookie("token",res.getToken()));
        }

        return new ResponseEntity<User>(res.getUser(),res.getStatus());
    }


    //register
    @PostMapping("/register")
    public int register(@RequestBody Map<String, Object> data) {
        int Id=(Integer) data.get("id");
        long id = (int)Id;
        //Long id = Id.longValue();
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

}