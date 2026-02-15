package com.poshanp.task.manager.infrastructure.repositories;

import com.poshanp.task.manager.application.repository.IUserRepository;
import com.poshanp.task.manager.domain.entities.User;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final JpaUserRepo jpaRepo;

    @Override
    public User findById(long id) {
        return 0;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    @Override
    public User save(User user) {
        return null;
    }
}
