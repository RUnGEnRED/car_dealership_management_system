package com.project.backend_app.repository;

import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.entity.enums.VehicleAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Vehicle} entity.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Finds a vehicle by its Vehicle Identification Number (VIN).
     *
     * @param vin The VIN of the vehicle.
     * @return An {@link Optional} containing the vehicle if found, or empty otherwise.
     */
    Optional<Vehicle> findByVin(String vin);

    /**
     * Finds all vehicles that are marked as active.
     *
     * @return A list of active vehicles.
     */
    List<Vehicle> findAllByActiveTrue();

    /**
     * Finds all vehicles with a specific availability status that are also marked as active.
     *
     * @param availability The availability status to filter by.
     * @return A list of active vehicles with the specified availability.
     */
    List<Vehicle> findAllByAvailabilityAndActiveTrue(VehicleAvailability availability);

    /**
     * Checks if a vehicle exists with the given VIN.
     *
     * @param vin The VIN to check.
     * @return True if a vehicle with the VIN exists, false otherwise.
     */
    Boolean existsByVin(String vin);
}