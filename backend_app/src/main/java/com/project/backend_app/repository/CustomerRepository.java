package com.project.backend_app.repository;

import com.project.backend_app.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Customer} entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a customer by their username.
     *
     * @param username The username of the customer.
     * @return An {@link Optional} containing the customer if found, or empty otherwise.
     */
    Optional<Customer> findByUsername(String username);

    /**
     * Finds a customer by their email address.
     *
     * @param email The email address of the customer.
     * @return An {@link Optional} containing the customer if found, or empty otherwise.
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Checks if a customer exists with the given username.
     *
     * @param username The username to check.
     * @return True if a customer with the username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a customer exists with the given email address.
     *
     * @param email The email address to check.
     * @return True if a customer with the email address exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}