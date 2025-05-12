package com.project.frontend_app.service.interf;

import com.project.frontend_app.model.Customer;
import com.project.frontend_app.model.Employee;

import java.util.List;
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

    Optional<Employee> authenticateEmployee(String email, String password);

    List<Customer> findAll();
}