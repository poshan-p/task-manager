package com.poshanp.task.manager.application.dtos.response;

import com.poshanp.task.manager.domain.constants.ErrorMessage;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorMessage message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorMessage message) {
        return new ApiResponse<>(false, null, message);
    }

    public ApiResponse(boolean success, T data, ErrorMessage message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorMessage getMessage() {
        return message;
    }

    public void setMessage(ErrorMessage message) {
        this.message = message;
    }

}