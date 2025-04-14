package com.project.frontend_app.service.impl;

import com.project.frontend_app.model.Customer;
import com.project.frontend_app.model.Request;
import com.project.frontend_app.model.Vehicle;
import com.project.frontend_app.model.enums.RequestStatus;
import com.project.frontend_app.service.interf.IRequestService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * TEST IMPLEMENTATION
 * In-memory implementation of the {@link IRequestService} for demonstration purposes.
 * This service handles vehicle purchase request submissions and simulations.
 * 
 * <p>Note: This is a simulation implementation for development/testing purposes.
 * In a production environment, this would connect to a real backend service.</p>
 */
public class InMemoryRequestService implements IRequestService {

    // TODO: CHANGE TO CONNECT TO REAL BACKEND
    // TODO: CHECK AND REFACTOR LOGIC TO REAL IMPLEMENTATION

    /** Thread-safe list to store all created requests */
    private final List<Request> requests = Collections.synchronizedList(new ArrayList<>());
    
    /** Random number generator for simulating backend responses */
    private final Random random = new Random();

    /**
     * Submits a vehicle purchase request for processing.
     * 
     * @param customer The customer making the purchase request
     * @param vehicle The vehicle being requested for purchase
     * @return true if the request was successfully submitted and accepted,
     *         false if the request was rejected or invalid
     * @throws IllegalArgumentException if customer or vehicle is null
     */
    @Override
    public boolean submitPurchaseRequest(Customer customer, Vehicle vehicle) {
        // Validate input parameters
        if (customer == null || vehicle == null) {
            throw new IllegalArgumentException("Customer and vehicle cannot be null");
        }

        // Check vehicle availability
        if (!vehicle.isAvailable()) {
            System.err.printf("Request rejected - Vehicle %s is unavailable%n", vehicle.getVin());
            return false;
        }

        // Create and store the new request
        Request newRequest = new Request(customer, vehicle);
        requests.add(newRequest);
        System.out.printf("Created new purchase request: %s%n", newRequest);

        // Simulate backend processing (50% success rate)
        boolean success = random.nextBoolean();
        System.out.printf("Simulated backend response: %s%n", success ? "ACCEPTED" : "REJECTED");

        // Update request and vehicle status based on simulation result
        if (success) {
            vehicle.setAvailable(false);
            newRequest.setStatus(RequestStatus.ACCEPTED);
            System.out.printf("Vehicle %s marked as sold%n", vehicle.getVin());
        } else {
            newRequest.setStatus(RequestStatus.REJECTED);
            System.out.printf("Request %s was rejected%n", newRequest.getId());
        }

        return success;
    }
}