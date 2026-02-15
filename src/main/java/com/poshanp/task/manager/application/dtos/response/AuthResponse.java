package com.poshanp.task.manager.application.dtos.response;

import lombok.*;

import java.time.OffsetDateTime;


@Getter
@Setter
public class AuthResponse extends UserResponse {
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthResponse(long id, String username, String email, OffsetDateTime createdAt, OffsetDateTime updatedAt, String accessToken, String refreshToken) {
        super(id, username, email, createdAt, updatedAt);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
