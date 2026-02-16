package com.poshanp.task.manager.application.exceptions;

import com.poshanp.task.manager.domain.constants.ErrorMessage;

public class TokenGenerationException extends BusinessException {
    public TokenGenerationException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
