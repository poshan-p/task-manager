package com.poshanp.task.manager.application.dtos.response;

import java.time.OffsetDateTime;

public class AuthResponse extends UserResponse {
    private String accessToken;
    private String refreshToken;

    public AuthResponse(long id, String username, String email, OffsetDateTime createdAt, OffsetDateTime updatedAt, String accessToken, String refreshToken) {
        super(id, username, email, createdAt, updatedAt);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
