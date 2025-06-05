package com.project.backend_app.exception;

import com.project.backend_app.dto.error.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class uses @ControllerAdvice to provide centralized exception handling across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ResourceNotFoundException.
     * Returns a 404 Not Found response.
     *
     * @param ex The ResourceNotFoundException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error details.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage()); // Log specific message
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(), // Current timestamp
                HttpStatus.NOT_FOUND.value(), // Standard HTTP status
                "Not Found", // Standard HTTP status phrase
                ex.getMessage(), // Standard error message
                request.getRequestURI() // Request URI
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException.
     * Returns a 400 Bad Request response.
     *
     * @param ex The BadRequestException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error details.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        logger.warn("Bad request: {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DuplicateResourceException.
     * Returns a 409 Conflict response.
     *
     * @param ex The DuplicateResourceException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error details.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        logger.warn("Duplicate resource: {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles MethodArgumentNotValidException (thrown when @Valid validation fails).
     * Returns a 400 Bad Request response with detailed validation errors.
     *
     * @param ex The MethodArgumentNotValidException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error details and validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Ensures Swagger documents this as 400
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });
        logger.warn("Validation errors: {}", errors);
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Input data is invalid. Please check the details.",
                request.getRequestURI(),
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles AuthenticationException (e.g., BadCredentialsException during login).
     * Returns a 401 Unauthorized response.
     *
     * @param ex The AuthenticationException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error details.
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class}) // Handle both general and specific
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Invalid username or password.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles AccessDeniedException (thrown by Spring Security, e.g., @PreAuthorize failures).
     * Returns a 403 Forbidden response.
     *
     * @param ex The AccessDeniedException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error details.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        logger.warn("Access denied: {} for path: {}", ex.getMessage(), request.getRequestURI());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "You do not have permission to access this resource.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


    /**
     * Handles generic RuntimeException as a fallback.
     * Returns a 500 Internal Server Error response.
     * It's important to log these exceptions in detail for debugging.
     *
     * @param ex The RuntimeException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing a generic error message.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("An unexpected runtime error occurred: {}", ex.getMessage(), ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles generic Exception as a last resort fallback.
     * Returns a 500 Internal Server Error response.
     *
     * @param ex The Exception instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("An unexpected general error occurred: {}", ex.getMessage(), ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred on the server.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}