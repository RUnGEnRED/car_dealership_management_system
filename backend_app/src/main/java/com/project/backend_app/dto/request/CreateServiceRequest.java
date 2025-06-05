package com.project.backend_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a vehicle service request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceRequest {

    @NotNull(message = "Vehicle ID cannot be null")
    private Long vehicleId;

    private String customerNotes; // Optional details for the service
}