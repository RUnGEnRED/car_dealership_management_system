package com.project.backend_app.entity.enums;

/**
 * Represents the type of request made by a customer.
 */
public enum RequestType {
    /**
     * A request to purchase a vehicle.
     */
    PURCHASE,

    /**
     * A request for vehicle servicing.
     */
    SERVICE,

    /**
     * A request for vehicle inspection.
     */
    INSPECTION
}