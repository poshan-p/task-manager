package com.poshanp.task.manager.application.dtos.response;

import com.poshanp.task.manager.domain.constants.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
}