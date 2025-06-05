package com.project.backend_app.controller;

import com.project.backend_app.dto.auth.CustomerRegisterRequest;
import com.project.backend_app.dto.auth.EmployeeRegisterRequest;
import com.project.backend_app.dto.auth.JwtResponse;
import com.project.backend_app.dto.auth.LoginRequest;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication and user registration endpoints.
 */
@CrossOrigin(origins = "*", maxAge = 3600) // Allow all origins for simplicity, adjust for production
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new customer.
     *
     * @param registerRequest The customer registration details.
     * @return ResponseEntity containing the registered customer's DTO.
     */
    @PostMapping("/register/customer")
    @Operation(summary = "Register a new customer",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            })
    public ResponseEntity<CustomerDto> registerCustomer(@Valid @RequestBody CustomerRegisterRequest registerRequest) {
        CustomerDto registeredCustomer = authService.registerCustomer(registerRequest);
        return new ResponseEntity<>(registeredCustomer, HttpStatus.CREATED);
    }

    /**
     * Registers a new employee.
     * This endpoint requires MANAGER role.
     *
     * @param registerRequest The employee registration details.
     * @return ResponseEntity containing the registered employee's DTO.
     */
    @PostMapping("/register/employee")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Register a new employee (Manager access required)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Employee registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have MANAGER role")
            })
    public ResponseEntity<EmployeeDto> registerEmployee(@Valid @RequestBody EmployeeRegisterRequest registerRequest) {
        EmployeeDto registeredEmployee = authService.registerEmployee(registerRequest);
        return new ResponseEntity<>(registeredEmployee, HttpStatus.CREATED);
    }

    /**
     * Authenticates a user and returns a JWT.
     *
     * @param loginRequest The login credentials.
     * @return ResponseEntity containing the JWT and user details.
     */
    @PostMapping("/signin")
    @Operation(summary = "Authenticate user and get JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            })
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}