package com.project.backend_app.service;

import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.dto.employee.UpdateEmployeeRequest;

import java.util.List;

/**
 * Service interface for employee management operations.
 */
public interface EmployeeService {

    /**
     * Retrieves an employee by their ID.
     *
     * @param id The ID of the employee.
     * @return DTO of the found employee.
     * @throws RuntimeException if the employee is not found.
     */
    EmployeeDto getEmployeeById(Long id);

    /**
     * Retrieves an employee by their username.
     *
     * @param username The username of the employee.
     * @return DTO of the found employee.
     * @throws RuntimeException if the employee is not found.
     */
    EmployeeDto getEmployeeByUsername(String username);

    /**
     * Retrieves all employees.
     *
     * @return A list of DTOs representing all employees.
     */
    List<EmployeeDto> getAllEmployees();

    /**
     * Updates an existing employee's profile.
     *
     * @param id The ID of the employee to update.
     * @param updateRequest DTO containing updated employee details.
     * @return DTO of the updated employee.
     * @throws RuntimeException if the employee is not found.
     */
    EmployeeDto updateEmployee(Long id, UpdateEmployeeRequest updateRequest);

    /**
     * Deactivates an employee (soft delete).
     *
     * @param id The ID of the employee to deactivate.
     * @throws RuntimeException if the employee is not found.
     */
    void deactivateEmployee(Long id);

    /**
     * Activates an employee.
     *
     * @param id The ID of the employee to activate.
     * @throws RuntimeException if the employee is not found.
     */
    void activateEmployee(Long id);

    /**
     * Updates the profile of the currently authenticated employee.
     * Certain fields like 'position' cannot be changed through this method.
     *
     * @param authenticatedUsername The username of the employee updating their profile.
     * @param updateRequest DTO containing updated employee details.
     * @return DTO of the updated employee.
     * @throws RuntimeException if the employee is not found.
     */
    EmployeeDto updateMyProfile(String authenticatedUsername, UpdateEmployeeRequest updateRequest);
}