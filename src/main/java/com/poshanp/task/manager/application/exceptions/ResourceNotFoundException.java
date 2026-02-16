package com.poshanp.task.manager.application.exceptions;

import com.poshanp.task.manager.domain.constants.ErrorMessage;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}

