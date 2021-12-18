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

    /**
     * create a new user
     * @param user
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("")
    public ResponseEntity<Long> createUser(@RequestBody User user, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<Long>(-1L,HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtil.getRole(token).equals("管理员")){
            return new ResponseEntity<Long>(-1L,HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<Long>(this.userService.createUser(user), HttpStatus.OK);
    }

    /**
     * get a user by userid
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<User>((User) null,HttpStatus.UNAUTHORIZED);
        }

        User user = this.userService.getUserById(userId);
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    /**
     * update a user information
     * @param user
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@RequestBody User user,@PathVariable("userId") Long userId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<User>((User) null,HttpStatus.UNAUTHORIZED);
        }

        this.userService.updateUserById(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * deleta a user
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable("userId") Long userId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<User>((User) null,HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtil.getRole(token).equals("管理员")){
            //管理员才可以删除
            return new ResponseEntity<List<User>>((List<User>) null,HttpStatus.UNAUTHORIZED);
        }
        this.userService.deleteUserById(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * get some users information
     * @param id_list
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/list")
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "id", required = false) List<Long> id_list, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<List<User>>((List<User>) null,HttpStatus.UNAUTHORIZED);
        }
        List<User> users = this.userService.getUserByIds(id_list);
        return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }

    /**
     * get some users information
     * @param id_list
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("/list")
    public ResponseEntity<List<User>> postUsers(@RequestBody List<Long> id_list, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<List<User>>((List<User>) null,HttpStatus.UNAUTHORIZED);
        }
        List<User> users = this.userService.getUserByIds(id_list);
        return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }

    /**
     * delete some users
     * @param id_list
     * @param token
     * @return
     * @throws Exception
     */
    @DeleteMapping("/list")
    public ResponseEntity deleteUsers(@RequestBody List<Long> id_list, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtil.getRole(token).equals("管理员")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(this.userService.deleteUserByIds(id_list));
    }

    /**
     * a user login
     * @param username
     * @param password
     * @param response
     * @return
     */
    @GetMapping("/signin")
    public ResponseEntity<User> signin(@RequestParam(name = "username", required = true) String username,
                                       @RequestParam(name = "password", required = true) String password,
                                       HttpServletResponse response
    ) {
        User user= new User();
        user.setUsername(username);
        user.setPassword(password);

        Login res = this.userService.login(user);

        if(res.getStatus().equals(HttpStatus.OK)) {
            Cookie tokenCookie = new Cookie("token",res.getToken());
            tokenCookie.setPath("/");//设置作用域
            response.addCookie(tokenCookie);

        }

        return new ResponseEntity<User>(res.getUser(),res.getStatus());
    }

    /**
     * a user logout
     * @param username
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/signout")
    public ResponseEntity signout(@RequestParam(name = "username", required = true) String username,
                                  HttpServletRequest request,
                                  HttpServletResponse response
    ) throws Exception {
        request.getSession().removeAttribute("token");
        request.getSession().invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * update a user password
     * @param data
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("/password")
    public ResponseEntity<Void> updateUserPassword(@RequestBody Map<String, Object> data,
                                                   //@RequestParam(name = "userId", required = true) Integer userId,
                                                   //@RequestParam(name = "oldPwd", required = true) String oldPwd,
                                                   //@RequestParam(name = "newPwd", required = true) String newPwd,
                                                   @CookieValue(value="token", defaultValue="") String token) throws Exception {
        long userId = Long.valueOf(data.get("userId").toString());
        String oldPwd = (String)data.get("oldPwd");
        String newPwd = (String)data.get("newPwd");

        JwtUtil jwtUtil = new JwtUtil();
        if(!jwtUtil.verify(token)){
            //token验证不通过
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }

        //if(jwtUtil.getUserId(token)!=userId){
        //    return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        //}
        return new ResponseEntity<Void>(this.userService.updateUserPassword(userId, oldPwd, newPwd));
    }

    /**
     * token verfiy api
     * @param token
     * @return
     */
    @GetMapping("/token")
    public Boolean tokenVerfiy(@RequestParam(name = "token", required = true) String token){
        JwtUtil jwtUtil = new JwtUtil();
        Boolean res = jwtUtil.verify(token);
        return res;
    }

    /**
     * get token role api
     * @param token
     * @return
     */
    @GetMapping("/token/role")
    public String getTokenRole(@RequestParam(name = "token", required = true) String token){
        JwtUtil jwtUtil = new JwtUtil();
        return jwtUtil.getRole(token);
    }

    /*
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
    */


}