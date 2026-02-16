package com.poshanp.task.manager.domain.constants;

public enum ErrorMessage {
    // Authentication Errors - 401 Unauthorized
    AUTH_INVALID_CREDENTIALS("Invalid email/username or password.", ErrorStatus.UNAUTHORIZED),
    AUTH_INVALID_TOKEN("Authentication token is invalid or expired.", ErrorStatus.UNAUTHORIZED),
    AUTH_UNAUTHORIZED("You don't have permission to perform this action.", ErrorStatus.FORBIDDEN),

    // User Not Found - 404 Not Found
    AUTH_USER_NOT_FOUND("User account not found.", ErrorStatus.NOT_FOUND),

    // Validation Errors - 400 Bad Request
    AUTH_INVALID_EMAIL_ADDRESS("Email address format is invalid.", ErrorStatus.BAD_REQUEST),
    AUTH_INVALID_USERNAME("Username must be 3-20 characters, start with a letter, and contain only letters, numbers, or underscores.", ErrorStatus.BAD_REQUEST),

    // Conflict Errors - 409 Conflict
    AUTH_USERNAME_ALREADY_EXISTS("An account with this username already exists.", ErrorStatus.CONFLICT),
    AUTH_EMAIL_ALREADY_EXISTS("An account with this email address already exists.", ErrorStatus.CONFLICT),

    // Registration/Creation Errors - 500 Internal Server Error
    AUTH_UNKNOWN_ERROR_WHILE_CREATING_USER("Failed to create user account. Please try again.", ErrorStatus.INTERNAL_SERVER_ERROR),
    REGISTRATION_FAILED("Registration failed. Please try again later.", ErrorStatus.INTERNAL_SERVER_ERROR),

    // Token Generation Errors - 500 Internal Server Error
    TOKEN_GENERATION_FAILED("Failed to generate authentication token. Please try again.", ErrorStatus.INTERNAL_SERVER_ERROR),
    TOKEN_INVALID_USER_DATA("Cannot generate token for invalid user data.", ErrorStatus.INTERNAL_SERVER_ERROR),

    // Task Not Found - 404 Not Found
    TASK_TASK_NOT_FOUND("Task not found.", ErrorStatus.NOT_FOUND),

    // Task Permission Errors - 403 Forbidden
    TASK_UNAUTHORIZED_ACCESS("You don't have permission to access this task.", ErrorStatus.FORBIDDEN),

    // Task Operation Errors - 500 Internal Server Error
    TASK_UNKNOWN_ERROR_WHILE_CREATING_TASK("Failed to create task. Please try again.", ErrorStatus.INTERNAL_SERVER_ERROR),
    TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS("Failed to retrieve tasks. Please try again.", ErrorStatus.INTERNAL_SERVER_ERROR),
    TASK_UNKNOWN_ERROR_WHILE_UPDATING_TASK("Failed to update task. Please try again.", ErrorStatus.INTERNAL_SERVER_ERROR),
    TASK_UNKNOWN_ERROR_WHILE_DELETING_TASK("Failed to delete task. Please try again.", ErrorStatus.INTERNAL_SERVER_ERROR),

    // Database Errors - 500 Internal Server Error
    DATABASE_OPERATION_FAILED("A database error occurred. Please try again.", ErrorStatus.INTERNAL_SERVER_ERROR),
    DATABASE_CONNECTION_ERROR("Unable to connect to the database. Please try again later.", ErrorStatus.INTERNAL_SERVER_ERROR),

    // General Errors
    VALIDATION_FAILED("Request validation failed.", ErrorStatus.BAD_REQUEST),
    BAD_REQUEST("The request is invalid.", ErrorStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("An unexpected error occurred. Please try again later.", ErrorStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final ErrorStatus status;

    ErrorMessage(String message, ErrorStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public ErrorStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return this.message;
    }
}