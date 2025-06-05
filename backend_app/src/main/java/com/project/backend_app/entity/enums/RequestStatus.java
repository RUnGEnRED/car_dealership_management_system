package com.project.backend_app.entity.enums;

/**
 * Represents the status of a customer's request.
 */
public enum RequestStatus {
    /**
     * The initial status of a new request, awaiting review.
     */
    PENDING,

    /**
     * The request has been approved by an employee.
     */
    ACCEPTED,

    /**
     * The request has been denied by an employee.
     */
    REJECTED
}