package com.project.backend_app.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for standard error responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error; // e.g., "Not Found", "Bad Request"
    private String message;
    private String path;
    private Map<String, List<String>> validationErrors; // For detailed validation messages

    /**
     * Constructor for general errors without validation details.
     * @param timestamp Timestamp of the error.
     * @param status HTTP status code.
     * @param error HTTP error phrase.
     * @param message Specific error message.
     * @param path Request path.
     */
    public ErrorResponseDto(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.validationErrors = null; // No validation errors for this constructor
    }
}