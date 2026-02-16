package com.poshanp.task.manager.infrastructure.repositories.mappers;

import com.poshanp.task.manager.domain.entities.User;
import com.poshanp.task.manager.infrastructure.entities.UserEntity;

import java.util.ArrayList;

public class UserMapper {
    public static User toEntity(UserEntity user) {
        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static UserEntity toDomain(User user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                new ArrayList<>()
        );
    }
}
