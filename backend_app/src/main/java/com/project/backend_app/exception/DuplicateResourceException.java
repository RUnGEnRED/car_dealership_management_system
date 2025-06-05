package com.project.backend_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for cases where an attempt is made to create a resource
 * that already exists (e.g., duplicate username or email).
 * Results in an HTTP 409 Conflict response.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     * @param message The detail message.
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateResourceException with the specified detail message and cause.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}