package com.poshanp.task.manager.infrastructure.entities;

import com.poshanp.task.manager.domain.enums.Status;
import com.poshanp.task.manager.domain.enums.TaskPriority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    private String title;
    private String description;
    private TaskPriority priority;
    private Status status;
    private Date dueDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
