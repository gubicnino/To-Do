package com.example.todo.todos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.todo.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean completed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    // Ustvarjen ob kreiranju
    @Column(name = "start_time", nullable = false, updatable = false)
    private LocalDateTime startTime;

    // Nastavljen ko je completed=true
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @PrePersist
    public void prePersist() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        // če kdo že kreira completed=true, nastav endTime takoj
        if (completed && endTime == null) {
            endTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        // ko postane completed=true, zapolni endTime
        if (completed && endTime == null) {
            endTime = LocalDateTime.now();
        }
        // če kdo odkljuka nazaj na false, lahko počisti (opcijsko)
        if (!completed && endTime != null) {
            endTime = null;
        }
    }
}
