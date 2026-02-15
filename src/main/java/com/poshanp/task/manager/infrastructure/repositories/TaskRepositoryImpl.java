package com.poshanp.task.manager.infrastructure.repositories;

import com.poshanp.task.manager.application.repository.ITaskRepository;
import com.poshanp.task.manager.domain.entities.Task;
import com.poshanp.task.manager.domain.enums.Status;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepositoryImpl implements ITaskRepository {
    @Override
    public Task findById(long taskId) {
        return null;
    }

    @Override
    public List<Task> findByUserId(long userId) {
        return List.of();
    }

    @Override
    public List<Task> findByUserIdAndStatus(long userId, Status status) {
        return List.of();
    }

    @Override
    public List<Task> findOverdueTasksByUserId(long userId) {
        return List.of();
    }

    @Override
    public long countByUserIdAndStatus(long userId, Status status) {
        return 0;
    }

    @Override
    public boolean existsByIdAndUserId(Long id, Long userId) {
        return false;
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {

    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public Task save(Task task) {
        return null;
    }
}
