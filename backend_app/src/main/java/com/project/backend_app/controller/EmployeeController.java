package com.project.backend_app.controller;

import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.dto.employee.UpdateEmployeeRequest;
import com.project.backend_app.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for employee-related operations.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employees", description = "Endpoints for managing employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves a list of all employees.
     * Requires MANAGER role.
     *
     * @return ResponseEntity containing a list of EmployeeDto.
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get all employees (Manager access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved list of employees"))
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Retrieves a specific employee by their ID.
     * Requires MANAGER role.
     *
     * @param id The ID of the employee.
     * @return ResponseEntity containing the EmployeeDto.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get employee by ID (Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the employee to retrieve", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
                    @ApiResponse(responseCode = "404", description = "Employee not found")
            })
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * Updates an employee's information.
     * Requires MANAGER role.
     *
     * @param id The ID of the employee to update.
     * @param updateRequest The DTO containing update information.
     * @return ResponseEntity containing the updated EmployeeDto.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Update employee information (Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the employee to update", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Employee not found")
            })
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRequest updateRequest) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, updateRequest);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * Deactivates an employee (soft delete).
     * Requires MANAGER role.
     *
     * @param id The ID of the employee to deactivate.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Deactivate an employee (Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the employee to deactivate", required = true),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Employee deactivated successfully"),
                    @ApiResponse(responseCode = "404", description = "Employee not found")
            })
    public ResponseEntity<Void> deactivateEmployee(@PathVariable Long id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the profile of the currently authenticated employee.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param authentication The authentication object for the current user.
     * @return ResponseEntity containing the EmployeeDto.
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Get current employee's profile (Employee/Manager access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved employee profile"))
    public ResponseEntity<EmployeeDto> getCurrentEmployeeProfile(Authentication authentication) {
        String username = authentication.getName();
        EmployeeDto employee = employeeService.getEmployeeByUsername(username);
        return ResponseEntity.ok(employee);
    }

    /**
     * Updates the profile of the currently authenticated employee.
     * Position cannot be changed via this endpoint.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param updateRequest The DTO containing update information (position will be ignored).
     * @param authentication The authentication object for the current user.
     * @return ResponseEntity containing the updated EmployeeDto.
     */
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Update current employee's profile (Employee/Manager access, position cannot be changed)",
            description = "Employees can update their own information. Position cannot be changed via this endpoint.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Employee not found")
            })
    public ResponseEntity<EmployeeDto> updateCurrentEmployeeProfile(@Valid @RequestBody UpdateEmployeeRequest updateRequest, Authentication authentication) {
        String username = authentication.getName();
        EmployeeDto updatedEmployee = employeeService.updateMyProfile(username, updateRequest);
        return ResponseEntity.ok(updatedEmployee);
    }
}