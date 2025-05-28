package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class CommentForm {

    private int id;
    private int report_id;
    @NotBlank(message = "コメントを入力してください")
    @Length(message = "50文字以内で入力してください",min=0,max=50)
    private String content;
    private Date createdDate;
}