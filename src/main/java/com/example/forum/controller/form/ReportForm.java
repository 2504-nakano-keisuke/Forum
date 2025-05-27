package com.example.forum.controller.form;

import lombok.Data;

import java.util.Date;

@Data
public class ReportForm {

    private int id;
    private String content;
    private Date createdDate;
    private Date updatedDate;
}
