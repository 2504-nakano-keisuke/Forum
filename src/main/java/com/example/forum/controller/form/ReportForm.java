package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class ReportForm {

    private int id;
    @NotBlank(message = "投稿内容を入力してください")
    @Length(min=0,max=50,message = "50文字以内で入力してください")
    private String content;
    private Date createdDate;
    private Date updatedDate;
}
