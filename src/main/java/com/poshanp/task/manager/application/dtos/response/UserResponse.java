package com.poshanp.task.manager.application.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String username;
    private String email;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
