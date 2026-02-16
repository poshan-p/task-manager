package com.poshanp.task.manager.domain.entities;

import java.time.Instant;

public class RefreshToken {
    private Long id;
    private String token;
    private User user;
    private Instant expiryDate;
    private boolean revoked;
    private Instant createdAt;
    private Instant revokedAt;
    private String replacedByToken; // For audit trail

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getReplacedByToken() {
        return replacedByToken;
    }

    public void setReplacedByToken(String replacedByToken) {
        this.replacedByToken = replacedByToken;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }

    public static RefreshToken create(String token, User user, long expirationMs) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(expirationMs));
        refreshToken.setRevoked(false);
        refreshToken.setCreatedAt(Instant.now());
        return refreshToken;
    }

    public void revoke(String replacedBy) {
        this.revoked = true;
        this.revokedAt = Instant.now();
        this.replacedByToken = replacedBy;
    }
}