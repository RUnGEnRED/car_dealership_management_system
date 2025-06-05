package com.project.backend_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for cases where a request is malformed or invalid.
 * Results in an HTTP 400 Bad Request response.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new BadRequestException with the specified detail message.
     * @param message The detail message.
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new BadRequestException with the specified detail message and cause.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}