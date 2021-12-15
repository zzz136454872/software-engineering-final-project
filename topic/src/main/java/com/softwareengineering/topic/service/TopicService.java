package com.softwareengineering.topic.service;

import com.softwareengineering.topic.mapper.TopicMapper;

import com.softwareengineering.topic.pojo.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TopicService {
    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private RestTemplate restTemplate;

    public Boolean tokenVerifyTopic(String token) {
        String Url = "http://user-login-register/user/token?token=" + token;
        return this.restTemplate.getForObject(Url, Boolean.class);
    }

    public String getTokenRole(String token) {
        String Url = "http://user-login-register/user/token/role?token=" + token;

        return this.restTemplate.getForObject(Url, String.class);
    }

    public long createTopic(Topic topic){
        List<Topic> num = this.topicMapper.selectAll();
        //设置合适的id，id肯定为 [1,all_num+1]之间的一个
        for(int i=1;i<=num.size();i++){
            if(this.topicMapper.selectByPrimaryKey(i)==null){
                long id = (int) i;
                topic.setId(id);
                this.topicMapper.insert(topic);
                break;
            }
        }
        return topic.getId();
    }

    public Topic getTopicById(Long id) {
        return this.topicMapper.selectByPrimaryKey(id);
    }

    public void updateTopicById(Topic topic) {
        this.topicMapper.updateByPrimaryKeySelective(topic);
    }

    public List<Topic> getTopicByIds(List<Long> ids) {
        List<Topic> topics = new ArrayList<>();
        if(ids==null){
            //如果ids为空，则返回全部列表
            return this.topicMapper.selectAll();
        }
        ids.forEach(id -> {
            // 我们测试多次查询，
            topics.add(getTopicById(id));
        });
        return topics;
    }

    public Topic getTopicByStudentId(Long id) {
        List<Topic> topics;
        Topic topic = new Topic();
        //根据student_id查询而不是topic_id查询
        topic.setStudentId(id);
        topics = this.topicMapper.select(topic);
        if(topics.size()!=1){
            //没有找到
            return (Topic) null;
        }
        return topics.get(0);
    }

    public Topic getTopicByTeacherId(Long id) {
        List<Topic> topics = new ArrayList<>();
        Topic topic = new Topic();
        //根据teacher_id查询而不是topic_id查询
        topic.setTeacherId(id);
        topics = this.topicMapper.select(topic);
        if(topics.size()!=1){
            return (Topic) null;
        }
        return topics.get(0);
    }

    public HttpStatus setChosenTopic(Long userId, Long topicId){
        Topic topic = new Topic();
        topic.setId(topicId);
        Topic res = this.topicMapper.selectByPrimaryKey(topicId);
        if(res==(Topic) null){
            // 请求的课题不存在，根据课题号查无此课题
            return HttpStatus.BAD_REQUEST;
        }
        if(res.getStudentId()!=null){
            // 请求的课题已被选
            return HttpStatus.BAD_REQUEST;
        }
        topic.setStudentId(userId);
        this.topicMapper.updateByPrimaryKeySelective(topic);
        return HttpStatus.OK;
    }


}
