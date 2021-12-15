package com.softwareengineering.topic.mapper;

import com.softwareengineering.topic.pojo.Topic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicMapper extends tk.mybatis.mapper.common.Mapper<Topic>{
}
