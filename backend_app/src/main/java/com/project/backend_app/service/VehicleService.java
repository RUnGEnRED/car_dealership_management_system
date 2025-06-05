package com.project.backend_app.service;

import com.project.backend_app.dto.vehicle.CreateVehicleRequest;
import com.project.backend_app.dto.vehicle.UpdateVehicleRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;

import java.util.List;

/**
 * Service interface for vehicle management operations.
 */
public interface VehicleService {

    /**
     * Creates a new vehicle.
     *
     * @param createRequest DTO containing details for the new vehicle.
     * @return DTO of the created vehicle.
     */
    VehicleDto createVehicle(CreateVehicleRequest createRequest);

    /**
     * Retrieves a vehicle by its ID.
     * Only active vehicles are returned.
     *
     * @param id The ID of the vehicle.
     * @return DTO of the found vehicle.
     * @throws RuntimeException if the vehicle is not found or not active.
     */
    VehicleDto getVehicleById(Long id);

    /**
     * Retrieves all active vehicles.
     *
     * @return A list of DTOs representing active vehicles.
     */
    List<VehicleDto> getAllActiveVehicles();

    /**
     * Retrieves all active vehicles that are currently available for purchase.
     *
     * @return A list of DTOs representing active and available vehicles.
     */
    List<VehicleDto> getAllActiveAvailableVehicles();

    /**
     * Updates an existing vehicle.
     *
     * @param id The ID of the vehicle to update.
     * @param updateRequest DTO containing updated vehicle details.
     * @return DTO of the updated vehicle.
     * @throws RuntimeException if the vehicle is not found.
     */
    VehicleDto updateVehicle(Long id, UpdateVehicleRequest updateRequest);

    /**
     * Deactivates a vehicle (soft delete).
     *
     * @param id The ID of the vehicle to deactivate.
     * @throws RuntimeException if the vehicle is not found.
     */
    void deactivateVehicle(Long id);

    /**
     * Activates a vehicle.
     *
     * @param id The ID of the vehicle to activate.
     * @throws RuntimeException if the vehicle is not found.
     */
    void activateVehicle(Long id);
}