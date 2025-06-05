package com.project.backend_app.dto.vehicle;

import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.entity.enums.VehicleCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for representing vehicle data in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private String vin;
    private String make;
    private String model;
    private int productionYear;
    private BigDecimal price;
    private VehicleCondition vehicleCondition; // Send enum directly, or String representation
    private VehicleAvailability availability;   // Send enum directly, or String representation
    private Long ownerId; // ID of the customer who owns the vehicle, can be null
    private boolean active;
}