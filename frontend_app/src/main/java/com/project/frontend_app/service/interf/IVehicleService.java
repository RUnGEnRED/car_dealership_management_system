package com.project.frontend_app.service.interf;

import com.project.frontend_app.model.Vehicle;
import java.util.List;
import java.util.Optional;


/**
 * Interface for vehicle services in the car dealership management system.
 * Defines contract for vehicle-related operations including retrieval and management.
 */
public interface IVehicleService {
    /**
     * Finds all vehicles marked as available in the dealership inventory.
     *
     * @return A list of all available vehicles in the system.
     */
    List<Vehicle> findAllAvailable();

    /**
     * Finds a specific vehicle by its Vehicle Identification Number (VIN).
     *
     * @param vin The unique VIN of the vehicle to search for.
     * @return An Optional containing the vehicle if found, empty otherwise.
     */
    Optional<Vehicle> findByVin(String vin);

    // TODO: Add vehicle management methods for Employee/Admin roles
    // Vehicle saveVehicle(Vehicle vehicle); // Add later for Employees/Admins
    // Vehicle updateVehicle(Vehicle vehicle); // Add later for Employees/Admins
    // void deleteVehicle(String vin); // Add later for Employees/Admins
}