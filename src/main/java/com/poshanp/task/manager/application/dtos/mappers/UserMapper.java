package com.poshanp.task.manager.application.dtos.mappers;

import com.poshanp.task.manager.application.dtos.request.RegisterRequest;
import com.poshanp.task.manager.application.dtos.response.AuthResponse;
import com.poshanp.task.manager.application.dtos.response.UserResponse;
import com.poshanp.task.manager.domain.entities.User;

public class UserMapper {
    public static AuthResponse toAuthResponseDto(User user, String accessToken, String refreshToken) {
        return new AuthResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                accessToken,
                refreshToken);
    }

    public static UserResponse toUserResponseDto(User user) {
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public static User toEntity(RegisterRequest request, String hashedPassword) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(hashedPassword);

        return user;
    }
}
