package com.project.backend_app.entity;

import com.project.backend_app.entity.enums.RequestStatus;
import com.project.backend_app.entity.enums.RequestType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a customer's request within the car showroom system.
 * This could be a request for purchase, service, or inspection.
 */
@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer who made this request.
     * A request must be associated with a customer.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * The vehicle to which this request pertains.
     * A request must be associated with a vehicle.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /**
     * The employee who is assigned to or has handled this request.
     * This can be null if no employee has been assigned yet.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee assignedEmployee;

    /**
     * The type of the request (e.g., PURCHASE, SERVICE, INSPECTION).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    /**
     * The current status of the request (e.g., PENDING, ACCEPTED, REJECTED).
     * Defaults to PENDING.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    /**
     * Optional notes provided by the customer regarding the request.
     */
    @Lob // For potentially longer text
    private String customerNotes;

    /**
     * The timestamp when the request was created.
     * Automatically set when the entity is first persisted.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the request was last updated.
     * Automatically set on update.
     */
    private LocalDateTime updatedAt;

    /**
     * Sets the createdAt field before the entity is persisted for the first time.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Sets the updatedAt field before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}