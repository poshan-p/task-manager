package com.poshanp.task.manager.presentation.utils;

import com.poshanp.task.manager.application.exceptions.UnauthorizedException;
import com.poshanp.task.manager.domain.constants.ErrorMessage;
import org.springframework.security.core.Authentication;

public class AuthUtility {
    public static long getUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_CREDENTIALS);
        }

        return (Long) authentication.getPrincipal();
    }
}

