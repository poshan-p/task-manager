package com.poshanp.task.manager.application.exceptions;

import com.poshanp.task.manager.domain.constants.ErrorMessage;
import com.poshanp.task.manager.domain.constants.ErrorStatus;

public class BusinessException extends RuntimeException {
    private final ErrorMessage errorMessage;
    private final ErrorStatus status;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
        this.status = errorMessage.getStatus();
    }

    public BusinessException(String message, ErrorStatus status) {
        super(message);
        this.errorMessage = null;
        this.status = status;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public ErrorStatus getStatus() {
        return status;
    }
}