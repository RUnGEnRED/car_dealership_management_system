package com.project.backend_app.entity.enums;

/**
 * Represents the availability status of a vehicle in the showroom.
 */
public enum VehicleAvailability {
    /**
     * The vehicle is available for purchase or other requests.
     */
    AVAILABLE,

    /**
     * The vehicle is currently reserved, typically due to a pending purchase request.
     */
    RESERVED,

    /**
     * The vehicle has been sold.
     */
    SOLD
}