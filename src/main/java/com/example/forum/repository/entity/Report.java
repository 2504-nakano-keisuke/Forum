package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "reports")
@Data
public class Report {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    @Column(insertable = false, updatable = false, name="created_date")
    private Date createdDate;

}
