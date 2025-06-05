package com.project.backend_app.entity;

import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.entity.enums.VehicleCondition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a vehicle in the car showroom system.
 */
@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Vehicle Identification Number. Must be unique and not null.
     */
    @Column(unique = true, nullable = false)
    private String vin;

    private String make; // e.g., Toyota, Ford
    private String model; // e.g., Camry, Focus
    private int productionYear;
    private BigDecimal price;

    /**
     * The physical condition of the vehicle (e.g., NEW, USED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleCondition vehicleCondition;

    /**
     * The current availability status of the vehicle (e.g., AVAILABLE, RESERVED, SOLD).
     * Defaults to AVAILABLE.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleAvailability availability = VehicleAvailability.AVAILABLE;

    /**
     * The customer who owns this vehicle.
     * This is set when a vehicle is sold. Nullable.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id") // This will create an owner_id foreign key column in the vehicles table
    private Customer owner;

    /**
     * Version field for optimistic locking.
     * Helps manage concurrent updates to a vehicle.
     */
    @Version
    private Long version;

    /**
     * Indicates if the vehicle is active in the showroom's offer.
     * Used for soft delete functionality. Defaults to true.
     */
    private boolean active = true;
}