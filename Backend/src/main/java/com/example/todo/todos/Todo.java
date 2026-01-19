package com.example.todo.todos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.todo.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    public enum RecurrenceFrequency {
        NONE, DAILY, WEEKLY, MONTHLY
    }
  
    @JsonProperty("isRecurring")
    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_frequency")
    private RecurrenceFrequency recurrenceFrequency = RecurrenceFrequency.NONE;

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
