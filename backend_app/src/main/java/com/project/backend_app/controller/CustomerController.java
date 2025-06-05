package com.project.backend_app.controller;

import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.customer.UpdateCustomerRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.service.CustomerService;
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
 * Controller for customer-related operations.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Endpoints for managing customers")
@SecurityRequirement(name = "bearerAuth") // Indicates that endpoints here require JWT
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves a list of all customers.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @return ResponseEntity containing a list of CustomerDto.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Get all customers (Employee/Manager access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers"))
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Retrieves a specific customer by their ID.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param id The ID of the customer.
     * @return ResponseEntity containing the CustomerDto.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Get customer by ID (Employee/Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the customer to retrieve", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved customer"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            })
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    /**
     * Retrieves the profile of the currently authenticated customer.
     * Requires CUSTOMER role.
     *
     * @param authentication The authentication object for the current user.
     * @return ResponseEntity containing the CustomerDto.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Get current customer's profile (Customer access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved customer profile"))
    public ResponseEntity<CustomerDto> getCurrentCustomerProfile(Authentication authentication) {
        String username = authentication.getName();
        CustomerDto customer = customerService.getCustomerByUsername(username);
        return ResponseEntity.ok(customer);
    }

    /**
     * Updates the profile of the currently authenticated customer.
     * Requires CUSTOMER role.
     *
     * @param updateRequest The DTO containing update information.
     * @param authentication The authentication object for the current user.
     * @return ResponseEntity containing the updated CustomerDto.
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Update current customer's profile (Customer access)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            })
    public ResponseEntity<CustomerDto> updateCurrentCustomerProfile(@Valid @RequestBody UpdateCustomerRequest updateRequest, Authentication authentication) {
        String username = authentication.getName();
        CustomerDto currentCustomer = customerService.getCustomerByUsername(username);
        CustomerDto updatedCustomer = customerService.updateCustomerProfile(currentCustomer.getId(), updateRequest, username);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * Deactivates a customer (soft delete).
     * Requires MANAGER role.
     *
     * @param id The ID of the customer to deactivate.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Deactivate a customer (Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the customer to deactivate", required = true),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Customer deactivated successfully"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            })
    public ResponseEntity<Void> deactivateCustomer(@PathVariable Long id) {
        customerService.deactivateCustomer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves vehicles owned by the currently authenticated customer.
     * Requires CUSTOMER role.
     *
     * @param authentication The authentication object for the current user.
     * @return ResponseEntity containing a list of VehicleDto.
     */
    @GetMapping("/me/vehicles")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Get vehicles owned by current customer (Customer access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved owned vehicles"))
    public ResponseEntity<List<VehicleDto>> getMyOwnedVehicles(Authentication authentication) {
        String username = authentication.getName();
        CustomerDto customer = customerService.getCustomerByUsername(username);
        List<VehicleDto> vehicles = customerService.getOwnedVehicles(customer.getId());
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Retrieves vehicles owned by a specific customer.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param id The ID of the customer.
     * @return ResponseEntity containing a list of VehicleDto.
     */
    @GetMapping("/{id}/vehicles")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Get vehicles owned by a specific customer (Employee/Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the customer", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved owned vehicles"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            })
    public ResponseEntity<List<VehicleDto>> getOwnedVehiclesByCustomerId(@PathVariable Long id) {
        List<VehicleDto> vehicles = customerService.getOwnedVehicles(id);
        return ResponseEntity.ok(vehicles);
    }
}