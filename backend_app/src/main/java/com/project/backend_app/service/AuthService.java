package com.project.backend_app.service;

import com.project.backend_app.dto.auth.CustomerRegisterRequest;
import com.project.backend_app.dto.auth.EmployeeRegisterRequest;
import com.project.backend_app.dto.auth.JwtResponse;
import com.project.backend_app.dto.auth.LoginRequest;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.employee.EmployeeDto;

/**
 * Service interface for authentication and user registration operations.
 */
public interface AuthService {

    /**
     * Registers a new customer.
     *
     * @param registerRequest DTO containing customer registration details.
     * @return DTO of the registered customer.
     */
    CustomerDto registerCustomer(CustomerRegisterRequest registerRequest);

    /**
     * Registers a new employee.
     *
     * @param registerRequest DTO containing employee registration details.
     * @return DTO of the registered employee.
     */
    EmployeeDto registerEmployee(EmployeeRegisterRequest registerRequest);

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param loginRequest DTO containing login credentials.
     * @return JwtResponse containing the token and user details.
     */
    JwtResponse authenticateUser(LoginRequest loginRequest);
}