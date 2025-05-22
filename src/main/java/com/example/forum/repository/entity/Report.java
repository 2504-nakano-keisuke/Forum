package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "report")
@Data
public class Report {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

}
