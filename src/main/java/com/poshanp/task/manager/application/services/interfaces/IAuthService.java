package com.poshanp.task.manager.application.services.interfaces;

import com.poshanp.task.manager.application.dtos.request.LoginRequest;
import com.poshanp.task.manager.application.dtos.request.RegisterRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.AuthResponse;
import com.poshanp.task.manager.application.dtos.response.UserResponse;

public interface IAuthService {
    ApiResponse<AuthResponse> register(RegisterRequest request);

    ApiResponse<AuthResponse> login(LoginRequest request);

    ApiResponse<AuthResponse> refresh(String oldRefreshToken);

    ApiResponse<UserResponse> getCurrentUser(long userId);
    ApiResponse<Void> logout(String refreshToken);
    ApiResponse<Void> logoutAll(Long userId);
}
