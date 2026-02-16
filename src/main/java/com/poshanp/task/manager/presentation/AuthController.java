package com.poshanp.task.manager.presentation;

import com.poshanp.task.manager.application.dtos.request.LoginRequest;
import com.poshanp.task.manager.application.dtos.request.RegisterRequest;
import com.poshanp.task.manager.application.dtos.response.ApiResponse;
import com.poshanp.task.manager.application.dtos.response.AuthResponse;
import com.poshanp.task.manager.application.dtos.response.UserResponse;
import com.poshanp.task.manager.application.exceptions.UnauthorizedException;
import com.poshanp.task.manager.application.services.interfaces.IAuthService;
import com.poshanp.task.manager.domain.constants.ErrorMessage;
import com.poshanp.task.manager.presentation.utils.AuthUtility;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Arrays;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    private final IAuthService authService;

    @Value("${jwt.refresh.cookie.name:refresh_token}")
    private String refreshTokenCookieName;

    @Value("${jwt.refresh.cookie.max-age:604800}")
    private int refreshTokenCookieMaxAge;

    @Value("${jwt.refresh.cookie.secure:true}")
    private boolean refreshTokenCookieSecure;

    @Value("${jwt.refresh.cookie.domain:#{null}}")
    private String cookieDomain;

    private static final String REFRESH_TOKEN_COOKIE_PATH = "/api/v1/auth/refresh";

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        ApiResponse<AuthResponse> apiResponse = authService.login(request);

        if (apiResponse.isSuccess()) {
            setRefreshTokenCookie(response, apiResponse.getData().getRefreshToken());
            apiResponse.getData().setRefreshToken(null);
        }

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {

        ApiResponse<AuthResponse> apiResponse = authService.register(request);

        if (apiResponse.isSuccess()) {
            setRefreshTokenCookie(response, apiResponse.getData().getRefreshToken());
            apiResponse.getData().setRefreshToken(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(Authentication authentication) {
        long userId = AuthUtility.getUserId(authentication);
        ApiResponse<UserResponse> response = authService.getCurrentUser(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_TOKEN);
        }

        ApiResponse<AuthResponse> apiResponse = authService.refresh(refreshToken);

        if (apiResponse.isSuccess()) {
            setRefreshTokenCookie(response, apiResponse.getData().getRefreshToken());
            apiResponse.getData().setRefreshToken(null);
        }

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken != null) {
            authService.logout(refreshToken);
        }

        clearRefreshTokenCookie(response);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
            Authentication authentication,
            HttpServletResponse response) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_CREDENTIALS);
        }

        Long userId = (Long) authentication.getPrincipal();
        authService.logoutAll(userId);

        clearRefreshTokenCookie(response);

        return ResponseEntity.ok(ApiResponse.success(null));
    }


    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(refreshTokenCookieName, refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(refreshTokenCookieSecure);
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setMaxAge(refreshTokenCookieMaxAge);

        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }

        response.addCookie(cookie);

        response.addHeader("Set-Cookie",
                String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Lax",
                        refreshTokenCookieName,
                        refreshToken,
                        REFRESH_TOKEN_COOKIE_PATH,
                        refreshTokenCookieMaxAge
                )
        );
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(refreshTokenCookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(refreshTokenCookieSecure);
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setMaxAge(0);

        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }

        response.addCookie(cookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> refreshTokenCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}