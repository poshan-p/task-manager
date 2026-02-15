package com.poshanp.task.manager.application.repository;

import com.poshanp.task.manager.domain.entities.User;

public interface IUserRepository {
    User findById(long id);

    User findByEmail(String email);

    User findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User save(User user);
}
