package com.project.frontend_app.service.interf;

import com.project.frontend_app.model.Customer;
import java.util.Optional;


/**
 * Interface for authentication services in the car dealership management system.
 * Defines contract for authentication operations for different user types.
 */
public interface IAuthenticationService {
    /**
     * Authenticates a customer based on email and password.
     *
     * @param email The customer's email.
     * @param password The customer's password.
     * @return An Optional containing the authenticated Customer if successful, otherwise empty.
     */
    Optional<Customer> authenticateCustomer(String email, String password);

    // TODO: Add authentication methods for Employee
    // Optional<Employee> authenticateEmployee(String username, String password); // Add later for Employees
}