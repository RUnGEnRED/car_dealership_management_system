package com.project.frontend_app.service.interf;

import com.project.frontend_app.model.Customer;
import com.project.frontend_app.model.Request;
import com.project.frontend_app.model.Vehicle;


/**
 * Interface for request services in the car dealership management system.
 * Defines contract for vehicle purchase request operations.
 */
public interface IRequestService {

    /**
     * Submits a purchase request for a vehicle.
     * Simulates backend processing and marks vehicle as unavailable if successful.
     *
     * @param customer The customer making the request.
     * @param vehicle The vehicle being requested.
     * @return true if the simulated submission was successful, false otherwise.
     */
    boolean submitPurchaseRequest(Customer customer, Vehicle vehicle);

    // TODO: Add methods for service and inspection requests
    // List<Request> findRequestsByCustomer(Customer customer); // For future implementation
}