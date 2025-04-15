package com.alex.project.taskmanagerproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Size(min = 10, max = 100)
    private String title;

    @Column(nullable = false)
    @Size(min = 10, max = 3000)
    private String description;

    @Column
    private String priority;

    @Column(nullable = false)
    private String status = "TODO";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="project_id")
    private Project project;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
