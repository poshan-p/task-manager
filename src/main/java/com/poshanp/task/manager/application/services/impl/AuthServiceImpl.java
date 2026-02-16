package com.poshanp.task.manager.application.services.impl;

import com.poshanp.task.manager.application.dtos.mappers.UserMapper;
import com.poshanp.task.manager.application.dtos.request.LoginRequest;
import com.poshanp.task.manager.application.dtos.request.RegisterRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.AuthResponse;
import com.poshanp.task.manager.application.dtos.response.UserResponse;
import com.poshanp.task.manager.application.exceptions.*;
import com.poshanp.task.manager.application.repositories.IUserRepository;
import com.poshanp.task.manager.application.services.interfaces.IAuthService;
import com.poshanp.task.manager.application.services.interfaces.IPasswordService;
import com.poshanp.task.manager.application.services.interfaces.ITokenService;
import com.poshanp.task.manager.domain.constants.ErrorMessage;
import com.poshanp.task.manager.domain.entities.RefreshToken;
import com.poshanp.task.manager.domain.entities.User;

public class AuthServiceImpl implements IAuthService {
    private final IPasswordService passwordService;
    private final ITokenService tokenService;
    private final IUserRepository userRepository;

    public AuthServiceImpl(
            IPasswordService passwordService,
            ITokenService tokenService,
            IUserRepository userRepository) {
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        try {
            if (!User.isUsernameValid(request.getUsername())) {
                return ApiResponse.error(ErrorMessage.AUTH_INVALID_USERNAME);
            }

            if (!User.isEmailValid(request.getEmail())) {
                return ApiResponse.error(ErrorMessage.AUTH_INVALID_EMAIL_ADDRESS);
            }

            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BusinessException(ErrorMessage.AUTH_USERNAME_ALREADY_EXISTS);
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException(ErrorMessage.AUTH_EMAIL_ALREADY_EXISTS);
            }

            User newUser = UserMapper.toEntity(
                    request,
                    passwordService.encode(request.getPassword())
            );

            newUser = userRepository.save(newUser);

            if (newUser == null) {
                throw new BusinessException(ErrorMessage.AUTH_UNKNOWN_ERROR_WHILE_CREATING_USER);
            }

            String accessToken = tokenService.generateAccessToken(newUser);
            RefreshToken refreshToken = tokenService.createRefreshToken(newUser);

            return ApiResponse.success(
                    UserMapper.toAuthResponseDto(newUser, accessToken, refreshToken.getToken())
            );

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.REGISTRATION_FAILED);
        }
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        try {
            User user = User.isEmailValid(request.getLogin()) ?
                    userRepository.findByEmail(request.getLogin()) :
                    userRepository.findByUsername(request.getLogin());

            if (user == null) {
                throw new ResourceNotFoundException(ErrorMessage.AUTH_USER_NOT_FOUND);
            }

            boolean passwordMatches = passwordService.matches(
                    request.getPassword(),
                    user.getPasswordHash()
            );

            if (!passwordMatches) {
                throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_CREDENTIALS);
            }

            String accessToken = tokenService.generateAccessToken(user);
            RefreshToken refreshToken = tokenService.createRefreshToken(user);

            return ApiResponse.success(
                    UserMapper.toAuthResponseDto(user, accessToken, refreshToken.getToken())
            );

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_CREDENTIALS);
        }
    }

    @Override
    public ApiResponse<AuthResponse> refresh(String oldRefreshToken) {
        try {
            RefreshToken newRefreshToken = tokenService.rotateRefreshToken(oldRefreshToken);

            String newAccessToken = tokenService.generateAccessToken(newRefreshToken.getUser());

            return ApiResponse.success(
                    UserMapper.toAuthResponseDto(
                            newRefreshToken.getUser(),
                            newAccessToken,
                            newRefreshToken.getToken()
                    )
            );

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_TOKEN);
        }
    }

    @Override
    public ApiResponse<Void> logout(String refreshToken) {
        try {
            tokenService.revokeRefreshToken(refreshToken);
            return ApiResponse.success(null);
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<Void> logoutAll(Long userId) {
        try {
            tokenService.revokeAllUserRefreshTokens(userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<UserResponse> getCurrentUser(long userId) {
        try {
            if (userId <= 0) {
                throw new ResourceNotFoundException(ErrorMessage.AUTH_USER_NOT_FOUND);
            }

            User user = userRepository.findById(userId);

            if (user == null) {
                throw new ResourceNotFoundException(ErrorMessage.AUTH_USER_NOT_FOUND);
            }
            return ApiResponse.success(UserMapper.toUserResponseDto(user));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}