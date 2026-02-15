package com.poshanp.task.manager.domain.constants;

public enum ErrorMessage {
//    Auth Service Errors
    AUTH_USERNAME_ALREADY_EXISTS("User with the given username already exists."),
    AUTH_EMAIL_ALREADY_EXISTS("User with the given email already exists."),
    AUTH_UNKNOWN_ERROR_WHILE_CREATING_USER("An unknown error occurred while creating user."),
    AUTH_INVALID_EMAIL_ADDRESS("Invalid email address."),
    AUTH_INVALID_USERNAME("Invalid username. Username must be 3-20 characters, start with a letter, and contain only alphanumeric/underscores."),
    AUTH_USER_NOT_FOUND("User not found."),
    AUTH_INVALID_CREDENTIALS("Invalid email/username or password."),
    AUTH_INVALID_TOKEN("Invalid token."),



    TASK_UNKNOWN_ERROR_WHILE_CREATING_TASK("An unknown error occurred while creating task."),
    TASK_UNKNOWN_ERROR_WHILE_FETCHING_TASKS("An unknown error occurred while fetching tasks."),
    TASK_TASK_NOT_FOUND("Task not found."),
    TASK_UNKNOWN_ERROR_WHILE_UPDATING_TASK("An unknown error occurred while updating task.");


    private final String message;
    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
