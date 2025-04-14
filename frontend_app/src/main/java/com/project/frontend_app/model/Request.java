package com.project.frontend_app.model;

import com.project.frontend_app.model.enums.RequestStatus;
import com.project.frontend_app.model.enums.RequestType;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Represents a customer request in the car dealership system.
 * Can be either a purchase request or other types defined in RequestType enum.
 */
public class Request {
    /** Simple atomic ID generator for demo purposes */
    private static final AtomicLong idCounter = new AtomicLong();
    
    /** Unique identifier for the request */
    private Long id;
    
    /** Customer who made the request */
    private Customer customer;
    
    /** Vehicle the request is about */
    private Vehicle vehicle;
    
    /** Date when the request was made */
    private LocalDate requestDate;
    
    /** Type of the request (PURCHASE, TEST_DRIVE, etc.) */
    private RequestType requestType;
    
    /** Current status of the request (PENDING, APPROVED, etc.) */
    private RequestStatus status;

    /**
     * Creates a new purchase request.
     * @param customer Customer making the request
     * @param vehicle Vehicle being requested
     */
    public Request(Customer customer, Vehicle vehicle) {
        this.id = idCounter.incrementAndGet();
        this.customer = customer;
        this.vehicle = vehicle;
        this.requestDate = LocalDate.now();
        this.requestType = RequestType.PURCHASE;
        this.status = RequestStatus.PENDING;
    }

    // --- Getters ---
    
    /** @return The unique ID of this request */
    public Long getId() { return id; }
    
    /** @return The customer who made this request */
    public Customer getCustomer() { return customer; }
    
    /** @return The vehicle this request is about */
    public Vehicle getVehicle() { return vehicle; }
    
    /** @return The date when this request was made */
    public LocalDate getRequestDate() { return requestDate; }
    
    /** @return The type of this request */
    public RequestType getRequestType() { return requestType; }
    
    /** @return The current status of this request */
    public RequestStatus getStatus() { return status; }

    // --- Setters ---
    
    /**
     * Updates the status of this request.
     * @param status New status to set
     */
    public void setStatus(RequestStatus status) { this.status = status; }

    /**
     * Returns a string representation of this request.
     * @return Formatted string containing request details
     */
    @Override
    public String toString() {
        return "Request [ID=" + id + ", Type=" + requestType + ", Status=" + status +
                ", Customer=" + (customer != null ? customer.getEmail() : "N/A") +
                ", Vehicle=" + (vehicle != null ? vehicle.getVin() : "N/A") + "]";
    }
}