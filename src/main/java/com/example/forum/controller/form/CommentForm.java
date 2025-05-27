package com.example.forum.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class CommentForm {

    private int id;
    private int report_id;
    private String content;
    private Date createdDate;
}