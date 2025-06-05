package com.project.backend_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for cases where a requested resource is not found.
 * Results in an HTTP 404 Not Found response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * @param message The detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}