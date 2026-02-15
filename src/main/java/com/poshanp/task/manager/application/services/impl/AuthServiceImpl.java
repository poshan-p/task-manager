package com.poshanp.task.manager.application.services.impl;

import com.poshanp.task.manager.application.dtos.mappers.UserMapper;
import com.poshanp.task.manager.application.dtos.request.LoginRequest;
import com.poshanp.task.manager.application.dtos.request.RegisterRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.AuthResponse;
import com.poshanp.task.manager.application.dtos.response.UserResponse;
import com.poshanp.task.manager.application.repository.IUserRepository;
import com.poshanp.task.manager.application.services.interfaces.IAuthService;
import com.poshanp.task.manager.application.services.interfaces.IPasswordService;
import com.poshanp.task.manager.application.services.interfaces.ITokenService;
import com.poshanp.task.manager.domain.constants.ErrorMessage;
import com.poshanp.task.manager.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {
    private final IPasswordService passwordService;
    private final ITokenService tokenService;
    private final IUserRepository userRepository;

    public AuthServiceImpl(IPasswordService passwordService, ITokenService tokenService, IUserRepository userRepository) {
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        User newUser = UserMapper.toEntity(request, passwordService.encode(request.getPassword()));

        boolean exists = userRepository.existsByUsername(request.getUsername());
        if (exists) return ApiResponse.error(ErrorMessage.AUTH_USERNAME_ALREADY_EXISTS);
        exists = userRepository.existsByEmail(request.getEmail());
        if (exists) return ApiResponse.error(ErrorMessage.AUTH_EMAIL_ALREADY_EXISTS);

        if (!User.isUsernameValid(request.getUsername())) return ApiResponse.error(ErrorMessage.AUTH_INVALID_USERNAME);
        if (!User.isEmailValid(request.getEmail())) return ApiResponse.error(ErrorMessage.AUTH_INVALID_EMAIL_ADDRESS);

        newUser = userRepository.save(newUser);

        if (newUser == null) return ApiResponse.error(ErrorMessage.AUTH_UNKNOWN_ERROR_WHILE_CREATING_USER);

        String accessToken = tokenService.generateAccessToken(newUser);
        String refreshToken = tokenService.generateRefreshToken(newUser);

        return ApiResponse.success(UserMapper.toAuthResponseDto(newUser, accessToken, refreshToken));
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        User user = User.isEmailValid(request.getLogin()) ?
                userRepository.findByEmail(request.getLogin()) :
                userRepository.findByUsername(request.getLogin());

        if (user == null) {
            return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);
        }

        boolean passwordMatches = passwordService.matches(request.getPassword(), user.getPasswordHash());
        if (!passwordMatches) {
            return ApiResponse.error(ErrorMessage.AUTH_INVALID_CREDENTIALS);
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return ApiResponse.success(UserMapper.toAuthResponseDto(user, accessToken, refreshToken));
    }

    @Override
    public ApiResponse<AuthResponse> refresh(String oldRefreshToken) {
        Long userId = tokenService.validateRefreshTokenAndGetUserId(oldRefreshToken);
        if (userId == null) return ApiResponse.error(ErrorMessage.AUTH_INVALID_TOKEN);

        User user = userRepository.findById(userId);
        if (user == null) return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return ApiResponse.success(UserMapper.toAuthResponseDto(user, accessToken, refreshToken));
    }

    @Override
    public long validateToken(String token) {
        return tokenService.validateAccessTokenAndGetUserId(token);
    }

    @Override
    public ApiResponse<UserResponse> getCurrentUser(long userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return ApiResponse.error(ErrorMessage.AUTH_USER_NOT_FOUND);
        }
        return ApiResponse.success(UserMapper.toUserResponseDto(user));
    }
}
