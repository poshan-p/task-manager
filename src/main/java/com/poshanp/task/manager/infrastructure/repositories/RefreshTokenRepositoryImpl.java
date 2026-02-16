package com.poshanp.task.manager.infrastructure.repositories;

import com.poshanp.task.manager.application.repositories.IRefreshTokenRepository;
import com.poshanp.task.manager.domain.entities.RefreshToken;
import com.poshanp.task.manager.infrastructure.repositories.jpa.RefreshTokenJpaRepository;
import com.poshanp.task.manager.infrastructure.repositories.mappers.RefreshTokenMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenRepositoryImpl implements IRefreshTokenRepository {
    
    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        try {
            var entity = RefreshTokenMapper.toEntity(refreshToken);
            var saved = jpaRepository.save(entity);
            return RefreshTokenMapper.toDomain(saved);
        } catch (Exception e) {
            log.error("Error saving refresh token", e);
            throw e;
        }
    }

    @Override
    public RefreshToken findByToken(String token) {
        try {
            return jpaRepository.findByToken(token)
                    .map(RefreshTokenMapper::toDomain)
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error finding refresh token", e);
            return null;
        }
    }

    @Override
    public void deleteByToken(String token) {
        try {
            jpaRepository.deleteByToken(token);
        } catch (Exception e) {
            log.error("Error deleting refresh token", e);
            throw e;
        }
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        try {
            jpaRepository.deleteAllByUserId(userId);
        } catch (Exception e) {
            log.error("Error deleting all refresh tokens for user: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void deleteExpiredTokens() {
        try {
            jpaRepository.deleteExpiredTokens(Instant.now());
            log.debug("Expired refresh tokens deleted");
        } catch (Exception e) {
            log.error("Error deleting expired tokens", e);
            throw e;
        }
    }

    @Override
    public List<RefreshToken> findAllByUserId(Long userId) {
        try {
            return jpaRepository.findAllByUserId(userId).stream()
                    .map(RefreshTokenMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding refresh tokens for user: {}", userId, e);
            return List.of();
        }
    }

    @Override
    public int countActiveTokensByUserId(Long userId) {
        try {
            return jpaRepository.countActiveTokensByUserId(userId, Instant.now());
        } catch (Exception e) {
            log.error("Error counting active tokens for user: {}", userId, e);
            return 0;
        }
    }
}