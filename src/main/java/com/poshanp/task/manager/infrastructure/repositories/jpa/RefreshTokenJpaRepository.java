package com.poshanp.task.manager.infrastructure.repositories.jpa;

import com.poshanp.task.manager.infrastructure.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    
    Optional<RefreshTokenEntity> findByToken(String token);
    
    List<RefreshTokenEntity> findAllByUserId(Long userId);
    
    @Query("SELECT COUNT(rt) FROM RefreshTokenEntity rt WHERE rt.user.id = :userId AND rt.revoked = false AND rt.expiryDate > :now")
    int countActiveTokensByUserId(@Param("userId") Long userId, @Param("now") Instant now);
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.token = :token")
    void deleteByToken(@Param("token") String token);
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") Instant now);
}