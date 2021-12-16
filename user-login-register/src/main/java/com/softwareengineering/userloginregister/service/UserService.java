package com.softwareengineering.userloginregister.service;

import com.softwareengineering.userloginregister.mapper.UserMapper;
import com.softwareengineering.userloginregister.pojo.User;
import com.softwareengineering.userloginregister.pojo.Login;
import com.softwareengineering.userloginregister.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;
    /*
    public HttpStatus register(User user){
        //String role = user.getRole();
        if (!user.getRole().equals("student") || !user.getRole().equals("teacher")){
            //不能注册管理员
            return HttpStatus.BAD_REQUEST;
        }

        int userNum = queryNumByUsername(user.getUsername());
        if (userNum != 0){
            //用户名重复
            return HttpStatus.BAD_REQUEST;
        }
        String pw = user.getPassword();
        if (pw == null || pw.length() < 6){
            //密码长度过短
            return HttpStatus.BAD_REQUEST;
        }
        //应该存入密码的Hash值
        user.setPassword(SHA(user.getPassword(), "SHA-256"));
        if (userMapper.insertSelective(user)!=0){
            //插入成功
            return HttpStatus.OK;
        }else{
            //插入失败
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
    */

    public long createUser(User user){
        List<User> num = this.userMapper.selectAll();
        //设置合适的id，id肯定为 [1,all_num+1]之间的一个
        for(int i=1;i<=num.size()+1;i++){
            if(this.userMapper.selectByPrimaryKey(i)==(User) null){
                long id = (int) i;
                user.setId(id);
                this.userMapper.insert(user);
                break;
            }
        }
        return user.getId();
    }

    public User getUserById(Long id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    public void updateUserById(User user) {
        this.userMapper.updateByPrimaryKeySelective(user);
    }

    public void deleteUserById(Long id) {
        this.userMapper.deleteByPrimaryKey(id);
    }

    public List<User> getUserByIds(List<Long> ids) {
        List<User> users = new ArrayList<>();
        // 根据服务名称，获取服务实例
        // List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        // 因为只有一个UserService,因此我们直接get(0)获取
        // ServiceInstance instance = instances.get(0);
        // 获取ip和端口信息
        // String baseUrl = "http://"+instance.getHost() + ":" + instance.getPort()+"/user/";
        //String baseUrl = "http://user-service/user/";
        if(ids==null){
            //如果ids为空，则返回全部列表
            return this.userMapper.selectAll();
        }
        ids.forEach(id -> {
            // 我们测试多次查询，
            users.add(getUserById(id));
        });
        return users;
    }

    public HttpStatus deleteUserByIds(List<Long> ids) {
        if(ids==null){
            //如果ids为空，则返回请求错误
            return HttpStatus.BAD_REQUEST;
        }
        ids.forEach(id -> {
            User user = getUserById(id);
            if(!user.getRole().equals("管理员")){
                deleteUserById(id);
            }
        });
        return HttpStatus.OK;
    }

    public Login login(User user){
        Login login_response = new Login();
        int userNum = queryNumByUsername(user.getUsername());
        if (userNum == 0){
            //用户名不存在
            login_response.setStatus(HttpStatus.UNAUTHORIZED);
            return login_response;
        }

        if(this.userMapper.selectCount(user)==0){
            //密码错误
            login_response.setStatus(HttpStatus.UNAUTHORIZED);
            return login_response;
        }

        List<User> intact_user_list = this.userMapper.select(user);
        User intact_user = intact_user_list.get(0);

        String token = JwtUtil.sign(intact_user.getUsername(), intact_user.getId(), intact_user.getRole());
        if (token == null) {
            login_response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return login_response;
        }

        login_response.setUser(intact_user);
        login_response.setStatus(HttpStatus.OK);
        login_response.setToken(token);
        return login_response;

    }

    public HttpStatus updateUserPassword(Long id, String oldPwd, String newPwd){
        User user = this.userMapper.selectByPrimaryKey(id);
        if(user == (User) null){
            return HttpStatus.BAD_REQUEST;
        }
        if(!user.getPassword().equals(oldPwd)){
            return HttpStatus.UNAUTHORIZED;
        }
        user.setPassword(newPwd);

        //根据主键更新属性不为null的值
        this.userMapper.updateByPrimaryKeySelective(user);
        return HttpStatus.OK;
    }

    public boolean tokenVerify(String username, int userId, String role, String token){
        //主键不能为空
        if(userId==0 || role==null || username==null){
            return false;
        }

        JwtUtil jwtUtil = new JwtUtil();

        if(!jwtUtil.getUsername(token).equals(username) || !jwtUtil.getUserId(token).equals(userId) || !jwtUtil.getRole(token).equals(role)){
            return false;
        }

        boolean b = jwtUtil.verify(token);
        return b;
    }

    public int queryNumByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return this.userMapper.selectCount(user);
    }

    public Long queryIdByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        List<User> res = this.userMapper.select(user);
        return res.get(0).getId();
    }


    private String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密类型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 类型结果
                byte[] byteBuffer = messageDigest.digest();

                // 將 byte 转换为 string
                StringBuffer strHexString = new StringBuffer();
                // 遍历 byte buffer
                for (byte b : byteBuffer) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}

