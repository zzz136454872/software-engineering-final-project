package com.softwareengineering.topic.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table
@AllArgsConstructor
public class Topic {
    @Id
    private Long id = null;
    private Long teacherId = null;
    private Long studentId = null;
    private String title = null;
    private String difficulty = null;
    private String description = null;
    private String requirement = null;
    private String createTime = null;
    private String updateTime = null;

    public Topic () {}
}
