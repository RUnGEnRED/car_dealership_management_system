package com.project.frontend_app.service.impl;

import com.project.frontend_app.model.Address;
import com.project.frontend_app.model.Customer;
import com.project.frontend_app.service.interf.IAuthenticationService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * TEST IMPLEMENTATION
 * In-memory implementation of {@link IAuthenticationService} for demonstration purposes.
 * This service handles customer authentication.
 * <p>
 * <strong>WARNING:</strong> This implementation stores passwords in plain text and should NEVER be used
 * in production environments. For real applications, use proper password hashing.
 * <p>Note: This is a simulation implementation for development/testing purposes.
 * In a production environment, this would connect to a real backend service.</p>
 */
public class InMemoryAuthenticationService implements IAuthenticationService {

    // TODO: CHANGE THIS TO USE A REAL AUTHENTICATION SERVICE AND CONNECT TO A REAL BACKEND
    // TODO: CHECK AND REFACTOR LOGIC TO REAL IMPLEMENTATION

    // !! START TEST DATA FOR CUSTOMERS !!
    /**
     * In-memory storage for customer credentials.
     * Key: Lowercase email address
     * Value: Map containing Customer object and plain text password (for demo only)
     */
    private final Map<String, Map<String, Object>> customerCredentials = new HashMap<>();

    /**
     * Constructor that initializes the service with test customers.
     */
    public InMemoryAuthenticationService() {
        // INITIALIZE WITH TEST CUSTOMERS FOR DEMO PURPOSES
        Customer customer1 = new Customer("Jan", "Kowalski", "123456789", "jan.k@example.com",
                new Address("Kwiatowa", "10", null, "Warszawa", "00-001"));
        addTestCustomer(customer1, "password123");

        Customer customer2 = new Customer("Anna", "Nowak", "987654321", "anna.n@example.com",
                new Address("Leśna", "5", "2", "Kraków", "30-002"));
        addTestCustomer(customer2, "qwerty");

        Customer customer3 = new Customer("Jan", "Nowak", "123123123", "qwe",
                new Address("Nocna", "3", "5", "Warszawa", "00-001"));
        addTestCustomer(customer3, "qwe");
    }

    /**
     * Adds a test customer to the in-memory storage.
     * @param customer The customer to add
     * @param plainPassword The plain text password (for demo only - insecure)
     */
    private void addTestCustomer(Customer customer, String plainPassword) {
        Map<String, Object> data = new HashMap<>();
        data.put("customer", customer);
        data.put("password", plainPassword); // Store plain password for demo only
        customerCredentials.put(customer.getEmail().toLowerCase(), data);
    }
    // !! END TEST DATA FOR CUSTOMERS !!

    /**
     * Authenticates a customer based on the provided email and password.
     * 
     * @param email The customer's email address
     * @param password The customer's password
     * @return An Optional containing the authenticated Customer object, or an empty Optional if authentication fails
     */
    @Override
    public Optional<Customer> authenticateCustomer(String email, String password) {
        // Null check for input parameters
        if (email == null || password == null) {
            return Optional.empty();
        }

        // Retrieve stored credentials for the given email (case-insensitive)
        Map<String, Object> storedData = customerCredentials.get(email.toLowerCase());

        if (storedData != null) {
            String storedPassword = (String) storedData.get("password");
            
            // WARNING: This is insecure - plain text password comparison
            // In production, use: passwordEncoder.matches(password, storedPasswordHash)
            if (password.equals(storedPassword)) {
                return Optional.ofNullable((Customer) storedData.get("customer"));
            }
        }
        
        // Authentication failed
        return Optional.empty();
    }

    // TODO: Implement authentication for employees
}