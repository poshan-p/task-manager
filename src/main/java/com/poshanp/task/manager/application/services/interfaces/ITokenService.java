package com.poshanp.task.manager.application.services.interfaces;

import com.poshanp.task.manager.domain.entities.User;

public interface ITokenService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    Long validateAccessTokenAndGetUserId(String token);
    Long validateRefreshTokenAndGetUserId(String token);
}