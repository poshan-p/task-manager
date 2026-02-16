package com.poshanp.task.manager.infrastructure.repositories;

import com.poshanp.task.manager.application.repositories.ITaskRepository;
import com.poshanp.task.manager.domain.entities.Task;
import com.poshanp.task.manager.domain.enums.Status;
import com.poshanp.task.manager.infrastructure.repositories.jpa.TaskJpaRepository;
import com.poshanp.task.manager.infrastructure.repositories.mappers.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class TaskRepositoryImpl implements ITaskRepository {
    private final TaskJpaRepository jpaRepository;

    @Override
    public Task findById(long taskId) {
        return jpaRepository.findById(taskId).map(TaskMapper::toEntity).orElse(null);
    }

    @Override
    public List<Task> findByUserId(long userId) {
        return jpaRepository.findByUserId(userId).stream().map(TaskMapper::toEntity).toList();
    }

    @Override
    public List<Task> findByUserIdAndStatus(long userId, Status status) {
        return jpaRepository.findByUserIdAndStatus(userId, status).stream().map(TaskMapper::toEntity).toList();
    }

    @Override
    public List<Task> findOverdueTasksByUserId(long userId) {
        return jpaRepository.findOverdueTasksByUserId(userId, Status.DONE).stream().map(TaskMapper::toEntity).toList();
    }

    @Override
    public long countByUserIdAndStatus(long userId, Status status) {
        return jpaRepository.countByUserIdAndStatus(userId, status);
    }

    @Override
    public boolean existsByIdAndUserId(Long id, Long userId) {
        return jpaRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        jpaRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public Task save(Task task) {
        return TaskMapper.toEntity(jpaRepository.save(TaskMapper.toDomain(task)));
    }
}
