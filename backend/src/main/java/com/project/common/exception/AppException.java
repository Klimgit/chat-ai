package com.project.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for all business logic in the application.
 * All custom exceptions should inherit from this one.
 */
public class AppException extends RuntimeException {
    
    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}