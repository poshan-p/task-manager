package com.poshanp.task.manager.application.exceptions;

import com.poshanp.task.manager.domain.constants.ErrorMessage;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
    
}
