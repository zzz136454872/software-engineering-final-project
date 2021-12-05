package com.softwareengineering.userservice.mapper;

import com.softwareengineering.userservice.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<User>{
}