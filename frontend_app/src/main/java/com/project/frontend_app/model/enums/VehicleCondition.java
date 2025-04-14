package com.project.frontend_app.model.enums;


/**
 * Represents the condition state of a vehicle in the dealership inventory.
 * This enum is used to classify vehicles as either new or used.
 */
public enum VehicleCondition {
    /**
     * Brand-new vehicle that has never been owned or registered.
     * Typically comes with full manufacturer warranty.
     */
    NEW,
    
    /**
     * Pre-owned vehicle that has had at least one previous owner.
     * May come with remaining manufacturer warranty or dealership warranty.
     */
    USED
}