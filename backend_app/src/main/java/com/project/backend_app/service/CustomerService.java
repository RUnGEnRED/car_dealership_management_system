package com.project.backend_app.service;

import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.customer.UpdateCustomerRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;


import java.util.List;

/**
 * Service interface for customer management operations.
 */
public interface CustomerService {

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer.
     * @return DTO of the found customer.
     * @throws RuntimeException if the customer is not found.
     */
    CustomerDto getCustomerById(Long id);

    /**
     * Retrieves a customer by their username.
     *
     * @param username The username of the customer.
     * @return DTO of the found customer.
     * @throws RuntimeException if the customer is not found.
     */
    CustomerDto getCustomerByUsername(String username);

    /**
     * Retrieves all customers.
     *
     * @return A list of DTOs representing all customers.
     */
    List<CustomerDto> getAllCustomers();

    /**
     * Updates an existing customer's profile.
     *
     * @param customerId The ID of the customer to update.
     * @param updateRequest DTO containing updated customer details.
     * @param authenticatedUsername The username of the currently authenticated user, for authorization checks.
     * @return DTO of the updated customer.
     * @throws RuntimeException if the customer is not found or unauthorized.
     */
    CustomerDto updateCustomerProfile(Long customerId, UpdateCustomerRequest updateRequest, String authenticatedUsername);

    /**
     * Deactivates a customer (soft delete).
     *
     * @param id The ID of the customer to deactivate.
     * @throws RuntimeException if the customer is not found.
     */
    void deactivateCustomer(Long id);

    /**
     * Activates a customer.
     *
     * @param id The ID of the customer to activate.
     * @throws RuntimeException if the customer is not found.
     */
    void activateCustomer(Long id);

    /**
     * Retrieves all vehicles owned by a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of DTOs representing vehicles owned by the customer.
     * @throws RuntimeException if the customer is not found.
     */
    List<VehicleDto> getOwnedVehicles(Long customerId);
}