package com.softwareengineering.userloginregister.service;

import com.softwareengineering.userloginregister.mapper.UserMapper;
import com.softwareengineering.userloginregister.pojo.User;
import com.softwareengineering.userloginregister.util.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public int register(User user){
        //String role = user.getRole();
        if (!user.getRole().equals("student") || !user.getRole().equals("teacher")){
            //不能注册管理员
            return 501;
        }

        int userNum = queryByUsername(user.getUsername());
        if (userNum != 0){
            //用户名重复
            return 502;
        }
        String pw = user.getPassword();
        if (pw == null || pw.length() < 6){
            //密码长度过短
            return 503;
        }
        //应该存入密码的Hash值
        user.setPassword(SHA(user.getPassword(), "SHA-256"));
        if (userMapper.insertSelective(user)!=0){
            //插入成功
            return 200;
        }else{
            //插入失败
            return 504;
        }
    }


    public String login(User user){
        int userNum = queryByUsername(user.getUsername());
        if (userNum == 0){
            //用户名不存在
            return "500";
        }
        user.setPassword(SHA(user.getPassword(), "SHA-256"));
        if(this.userMapper.selectCount(user)==0){
            //密码错误
            return "501";
        }else{
            //正确
            String token = JwtUtil.sign(user.getName(), user.getId());
            if (token != null) {
                return "200";
            }
            return "200";
        }
    }

    //public int update(User user, String token)

    public int queryByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return this.userMapper.selectCount(user);
    }

    public User queryById(Long id) {
        return this.userMapper.selectByPrimaryKey(id);
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

