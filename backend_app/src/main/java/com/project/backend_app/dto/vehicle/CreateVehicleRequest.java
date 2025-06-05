package com.project.backend_app.dto.vehicle;

import com.project.backend_app.entity.enums.VehicleCondition;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new vehicle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    @NotBlank(message = "VIN cannot be blank")
    @Size(min = 17, max = 17, message = "VIN must be 17 characters long")
    private String vin;

    @NotBlank(message = "Make cannot be blank")
    @Size(max = 50, message = "Make can be at most 50 characters")
    private String make;

    @NotBlank(message = "Model cannot be blank")
    @Size(max = 50, message = "Model can be at most 50 characters")
    private String model;

    @NotNull(message = "Production year cannot be null")
    @Min(value = 1900, message = "Production year must be 1900 or later")
    private Integer productionYear;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Vehicle condition cannot be null")
    private VehicleCondition vehicleCondition;

    // Availability is typically set to AVAILABLE by default on creation.
    // Active status is typically true by default on creation.
}