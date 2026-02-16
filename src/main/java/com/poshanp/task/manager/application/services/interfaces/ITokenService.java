package com.poshanp.task.manager.application.services.interfaces;

import com.poshanp.task.manager.domain.entities.RefreshToken;
import com.poshanp.task.manager.domain.entities.User;

public interface ITokenService {
    // Access Token Operations
    String generateAccessToken(User user);
    Long validateAccessTokenAndGetUserId(String token);

    // Refresh Token Operations
    RefreshToken createRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    RefreshToken rotateRefreshToken(String oldToken);
    void revokeRefreshToken(String token);
    void revokeAllUserRefreshTokens(Long userId);

    // Token Utilities
    String extractTokenId(String token);
    boolean isAccessTokenExpired(String token);

    // Cleanup
    void cleanupExpiredTokens();
}