package com.poshanp.task.manager.infrastructure.repositories;

import com.poshanp.task.manager.application.repositories.IUserRepository;
import com.poshanp.task.manager.domain.entities.User;
import com.poshanp.task.manager.infrastructure.repositories.jpa.UserJpaRepository;
import com.poshanp.task.manager.infrastructure.repositories.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
@Transactional
public class UserRepositoryImpl implements IUserRepository {
    private final UserJpaRepository jpaRepository;

    @Override
    public User findById(long id) {
        return jpaRepository.findById(id).map(UserMapper::toEntity).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserMapper::toEntity).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(UserMapper::toEntity).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return UserMapper.toEntity(jpaRepository.save(UserMapper.toDomain(user)));
    }
}
