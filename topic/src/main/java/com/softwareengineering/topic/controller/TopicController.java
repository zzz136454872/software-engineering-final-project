package com.softwareengineering.topic.controller;

import com.softwareengineering.topic.pojo.Topic;
import com.softwareengineering.topic.pojo.setTopic;
import com.softwareengineering.topic.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("topic")
@Slf4j
public class TopicController {
    @Autowired
    private TopicService topicService;

    /**
     * 创建一个课题
     * create a topic
     * @param topic 要创建的课题
     * @param token cookie中的token
     * @return
     * @throws Exception
     */
    @PostMapping("")
    public ResponseEntity<Long> createTopic(@RequestBody Topic topic,
                                            @CookieValue(value="token", defaultValue="") String token) throws Exception {
        if(!this.topicService.tokenVerifyTopic(token)){
            //token验证不通过
            return new ResponseEntity<Long>(-1L, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Long>(this.topicService.createTopic(topic),HttpStatus.OK);
    }

    /**
     * 根据topicId查询一个课题
     * get topic by topicId
     * @param topicId 要查询的课题id
     * @param token cookie中的token
     * @return
     * @throws Exception
     */
    @GetMapping("/{topicId}")
    public ResponseEntity<Topic> getTopic(@PathVariable("topicId") Long topicId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        if(!this.topicService.tokenVerifyTopic(token)){
            //token验证不通过
            return new ResponseEntity<Topic>((Topic) null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Topic>(this.topicService.getTopicById(topicId),HttpStatus.OK);
    }

    /**
     * 更新一个课题的信息
     * update Topic by topicId
     * @param topic 新的课题信息
     * @param topicId 被更新的课题id
     * @param token cookie中的token
     * @return
     * @throws Exception
     */
    @PutMapping("/{topicId}")
    public ResponseEntity updateTopic(@RequestBody Topic topic,@PathVariable("topicId") Long topicId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        if(!this.topicService.tokenVerifyTopic(token)){
            //token验证不通过
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        this.topicService.updateTopicById(topic);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * get Topics by topicIds
     * @param id_list
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/list")
    public ResponseEntity<List<Topic>> getTopics(@RequestParam(name = "id", required = false) List<Long> id_list, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        if(!this.topicService.tokenVerifyTopic(token)){
            //token验证不通过
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<Topic> topics = this.topicService.getTopicByIds(id_list);
        return new ResponseEntity<List<Topic>>(topics,HttpStatus.OK);
    }

    /**
     * get choose topic by userId
     * 这里的userId实际上是student_id
     * 学生查看已选课题
     * @param studentId
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/choose")
    public ResponseEntity<Topic> getChosenTopic(@RequestParam(name = "userId", required = true) Long studentId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        if(!this.topicService.tokenVerifyTopic(token)){
            //token验证不通过
            return new ResponseEntity<Topic>((Topic) null, HttpStatus.UNAUTHORIZED);
        }

        Topic topic = this.topicService.getTopicByStudentId(studentId);

        if(topic == ((Topic) null)){
            // 如果学生没选课题，就返回这个
            return new ResponseEntity<Topic>((Topic) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Topic>(topic,HttpStatus.OK);
    }

    /**
     * student choose a topic by userId and topicId
     * 学生选择一个课题
     * @param topic
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("/choose")
    public ResponseEntity<Void> setChosenTopic(//@RequestParam(name = "userId", required = true) Long userId,
                                               //@RequestParam(name = "topicId", required = true) Long topicId,
                                               @RequestBody setTopic topic,
                                               @CookieValue(value="token", defaultValue="") String token) throws Exception {
        if(!this.topicService.tokenVerifyTopic(token)){
            //token验证不通过
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!this.topicService.getTokenRole(token).equals("学生")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(this.topicService.setChosenTopic(topic.getUserId(),topic.getTopicId()));
    }

    /**
     * get publish topic by userdId"
     * 老师查询一个课题
     * 这里的userId实际上是teacher_id
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/publish")
    public ResponseEntity<Topic> getPublishedTopic(@RequestParam(name = "userId", required = true) Long userId, @CookieValue(value="token", defaultValue="") String token) throws Exception {
        if(!this.topicService.tokenVerifyTopic(token)){
            //token验证不通过
            return new ResponseEntity<Topic>((Topic) null, HttpStatus.UNAUTHORIZED);
        }

        Topic topic = this.topicService.getTopicByTeacherId(userId);

        if(topic == ((Topic) null)){
            return new ResponseEntity<Topic>((Topic) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Topic>(topic,HttpStatus.OK);
    }
}
