package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int report_id;

    @Column
    private String content;

    @Column(insertable = false, updatable = false)
    private Date created_date;

}
