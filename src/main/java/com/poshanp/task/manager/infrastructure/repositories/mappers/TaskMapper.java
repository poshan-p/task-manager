package com.poshanp.task.manager.infrastructure.repositories.mappers;

import com.poshanp.task.manager.domain.entities.Task;
import com.poshanp.task.manager.infrastructure.entities.TaskEntity;

public class TaskMapper {
    public static Task toEntity(TaskEntity task) {
        return new Task(
                task.getId(),
                UserMapper.toEntity(task.getUser()),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public static TaskEntity toDomain(Task task) {
        return new TaskEntity(
                task.getId(),
                UserMapper.toDomain(task.getUser()),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
