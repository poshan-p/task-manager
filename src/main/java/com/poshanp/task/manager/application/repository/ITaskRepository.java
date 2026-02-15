package com.poshanp.task.manager.application.repository;

import com.poshanp.task.manager.domain.entities.Task;
import com.poshanp.task.manager.domain.enums.Status;
import java.util.List;

public interface ITaskRepository {
    Task findById(long taskId);
    List<Task> findByUserId(long userId);
    List<Task> findByUserIdAndStatus(long userId, Status status);
    List<Task> findOverdueTasksByUserId(long userId);
    long countByUserIdAndStatus(long userId, Status status);
    boolean existsByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
    Task update(Task task);
    Task save(Task task);
}
