package com.project.backend_app.repository;

import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.enums.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Employee} entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their username.
     *
     * @param username The username of the employee.
     * @return An {@link Optional} containing the employee if found, or empty otherwise.
     */
    Optional<Employee> findByUsername(String username);

    /**
     * Finds an employee by their email address.
     *
     * @param email The email address of the employee.
     * @return An {@link Optional} containing the employee if found, or empty otherwise.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Checks if an employee exists with the given username.
     *
     * @param username The username to check.
     * @return True if an employee with the username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if an employee exists with the given email address.
     *
     * @param email The email address to check.
     * @return True if an employee with the email address exists, false otherwise.
     */
    Boolean existsByEmail(String email);

    /**
     * Finds all employees with a specific position.
     *
     * @param position The position to filter by.
     * @return A list of employees with the given position.
     */
    List<Employee> findByPosition(Position position);
}