package com.poshanp.task.manager.infrastructure.exceptions;

import com.poshanp.task.manager.domain.constants.ErrorStatus;
import org.springframework.http.HttpStatus;

public class HttpStatusMapper {

    public static HttpStatus toHttpStatus(ErrorStatus status) {
        return switch (status) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;           // 400
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;         // 401
            case FORBIDDEN -> HttpStatus.FORBIDDEN;               // 403
            case NOT_FOUND -> HttpStatus.NOT_FOUND;               // 404
            case CONFLICT -> HttpStatus.CONFLICT;                 // 409
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR; // 500
        };
    }
}