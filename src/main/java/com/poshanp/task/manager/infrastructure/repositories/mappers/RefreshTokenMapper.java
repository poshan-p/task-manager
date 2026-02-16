package com.poshanp.task.manager.infrastructure.repositories.mappers;

import com.poshanp.task.manager.domain.entities.RefreshToken;
import com.poshanp.task.manager.infrastructure.entities.RefreshTokenEntity;

public class RefreshTokenMapper {
    
    public static RefreshToken toDomain(RefreshTokenEntity entity) {
        if (entity == null) return null;
        
        RefreshToken domain = new RefreshToken();
        domain.setId(entity.getId());
        domain.setToken(entity.getToken());
        domain.setUser(UserMapper.toEntity(entity.getUser()));
        domain.setExpiryDate(entity.getExpiryDate());
        domain.setRevoked(entity.isRevoked());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setRevokedAt(entity.getRevokedAt());
        domain.setReplacedByToken(entity.getReplacedByToken());
        return domain;
    }
    
    public static RefreshTokenEntity toEntity(RefreshToken domain) {
        if (domain == null) return null;
        
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUser(UserMapper.toDomain(domain.getUser()));
        entity.setExpiryDate(domain.getExpiryDate());
        entity.setRevoked(domain.isRevoked());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setRevokedAt(domain.getRevokedAt());
        entity.setReplacedByToken(domain.getReplacedByToken());
        return entity;
    }
}