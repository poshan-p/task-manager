package com.poshanp.task.manager.infrastructure.repositories.jpa;

import com.poshanp.task.manager.infrastructure.entities.TaskEntity;
import com.poshanp.task.manager.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserId(Long userId);

    List<TaskEntity> findByUserIdAndStatus(Long userId, Status status);

    @Query("SELECT t FROM TaskEntity t WHERE t.user.id = :userId " + "AND t.dueDate < CURRENT_DATE AND t.status != :status")
    List<TaskEntity> findOverdueTasksByUserId(@Param("userId") Long userId, @Param("status") Status status);

    long countByUserIdAndStatus(Long userId, Status status);

    boolean existsByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
