package com.project.backend_app.controller;

import com.project.backend_app.dto.vehicle.CreateVehicleRequest;
import com.project.backend_app.dto.vehicle.UpdateVehicleRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for vehicle-related operations.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "Endpoints for managing vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * Adds a new vehicle to the system.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param createRequest The DTO containing vehicle creation details.
     * @return ResponseEntity containing the created VehicleDto.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Add a new vehicle (Employee/Manager access)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vehicle created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            })
    public ResponseEntity<VehicleDto> createVehicle(@Valid @RequestBody CreateVehicleRequest createRequest) {
        VehicleDto createdVehicle = vehicleService.createVehicle(createRequest);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of all active vehicles.
     * This endpoint is public.
     *
     * @return ResponseEntity containing a list of VehicleDto.
     */
    @GetMapping
    @Operation(summary = "Get all active vehicles (Public access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved list of active vehicles"))
    public ResponseEntity<List<VehicleDto>> getAllActiveVehicles() {
        List<VehicleDto> vehicles = vehicleService.getAllActiveVehicles();
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Retrieves a list of all active and available vehicles.
     * This endpoint is public.
     *
     * @return ResponseEntity containing a list of VehicleDto.
     */
    @GetMapping("/available")
    @Operation(summary = "Get all active and available vehicles (Public access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved list of available vehicles"))
    public ResponseEntity<List<VehicleDto>> getAllActiveAvailableVehicles() {
        List<VehicleDto> vehicles = vehicleService.getAllActiveAvailableVehicles();
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Retrieves a specific vehicle by its ID.
     * This endpoint is public, but only shows active vehicles.
     *
     * @param id The ID of the vehicle.
     * @return ResponseEntity containing the VehicleDto.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID (Public access, shows active vehicles)",
            parameters = @Parameter(name = "id", description = "ID of the vehicle to retrieve", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicle"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found or inactive")
            })
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable Long id) {
        VehicleDto vehicle = vehicleService.getVehicleById(id); // Service method should ensure only active is returned or throw.
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Updates an existing vehicle's information.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param id The ID of the vehicle to update.
     * @param updateRequest The DTO containing update information.
     * @return ResponseEntity containing the updated VehicleDto.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Update vehicle information (Employee/Manager access)",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = @Parameter(name = "id", description = "ID of the vehicle to update", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            })
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable Long id, @Valid @RequestBody UpdateVehicleRequest updateRequest) {
        VehicleDto updatedVehicle = vehicleService.updateVehicle(id, updateRequest);
        return ResponseEntity.ok(updatedVehicle);
    }

    /**
     * Deactivates a vehicle (soft delete).
     * Requires MANAGER role.
     *
     * @param id The ID of the vehicle to deactivate.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Deactivate a vehicle (Manager access)",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = @Parameter(name = "id", description = "ID of the vehicle to deactivate", required = true),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vehicle deactivated successfully"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            })
    public ResponseEntity<Void> deactivateVehicle(@PathVariable Long id) {
        vehicleService.deactivateVehicle(id);
        return ResponseEntity.noContent().build();
    }
}