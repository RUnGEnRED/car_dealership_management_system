package com.project.backend_app.dto.vehicle;

import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.entity.enums.VehicleCondition;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating vehicle information. Fields are optional.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleRequest {

    // VIN is usually not updatable as it's a unique identifier.
    // If it needs to be, can be added later

    @Size(max = 50, message = "Make can be at most 50 characters")
    private String make;

    @Size(max = 50, message = "Model can be at most 50 characters")
    private String model;

    @Min(value = 1900, message = "Production year must be 1900 or later")
    private Integer productionYear;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    private VehicleCondition vehicleCondition;
    private VehicleAvailability availability; // Admins might change this
    private Boolean active; // Admins might change this
}