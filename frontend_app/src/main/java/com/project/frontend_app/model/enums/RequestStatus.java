package com.project.frontend_app.model.enums;


/**
 * Represents the status of a request in the car dealership management system.
 * Each status indicates a different stage in the request lifecycle.
 */
public enum RequestStatus {
    /**
     * Request is awaiting review by dealership staff.
     */
    PENDING,

    /**
     * Request has been approved by dealership staff.
     * For service requests: service has been scheduled
     * For purchase requests: sale is being finalized
     */
    ACCEPTED,

    /**
     * Request has been rejected by dealership staff.
     */
    REJECTED
}