package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

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
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(insertable = false, name="updated_date")
    private Timestamp updatedDate;
}
