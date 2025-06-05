package com.project.backend_app.dto.request;

import com.project.backend_app.entity.enums.RequestStatus;
import com.project.backend_app.entity.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing customer request data in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private Long customerId;
    private String customerUsername; // For easier display
    private Long vehicleId;
    private String vehicleVin; // For easier display
    private Long assignedEmployeeId;
    private String assignedEmployeeUsername; // For easier display
    private RequestType requestType;
    private RequestStatus status;
    private String customerNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}