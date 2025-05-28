package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class CommentForm {

    private int id;
    private int report_id;
    @NotBlank(message = "コメントを入力してください")
    private String content;
    private Date createdDate;
}