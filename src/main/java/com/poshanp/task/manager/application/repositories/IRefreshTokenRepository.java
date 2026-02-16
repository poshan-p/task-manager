package com.poshanp.task.manager.application.repositories;

import com.poshanp.task.manager.domain.entities.RefreshToken;

import java.util.List;

public interface IRefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    RefreshToken findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByUserId(Long userId);
    void deleteExpiredTokens();
    List<RefreshToken> findAllByUserId(Long userId);
    int countActiveTokensByUserId(Long userId);
}