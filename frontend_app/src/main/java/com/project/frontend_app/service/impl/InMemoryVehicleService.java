package com.project.frontend_app.service.impl;

import com.project.frontend_app.model.Vehicle;
import com.project.frontend_app.model.enums.VehicleCondition;
import com.project.frontend_app.service.interf.IVehicleService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * TEST IMPLEMENTATION
 * In-memory implementation of {@link IVehicleService} for demonstration purposes.
 * This service uses a ConcurrentHashMap for thread-safe operations.
 *
 * <p>Note: This is a simulation implementation for development/testing purposes.
 * In a production environment, this would connect to a real backend service.</p>
 */
public class InMemoryVehicleService implements IVehicleService {

    // TODO: CHANGE TO CONNECT TO REAL BACKEND
    // TODO: CHECK AND REFACTOR LOGIC TO REAL IMPLEMENTATION

    // !! START TEST DATA FOR VEHICLES !!
    /**
     * Thread-safe storage for vehicles, using VIN as the key
     */
    private final Map<String, Vehicle> vehicles = new ConcurrentHashMap<>();

    /**
     * Initializes service with test data
     */
    public InMemoryVehicleService() {
        // Initialize with sample vehicles
        addTestVehicle(new Vehicle("VIN001", "Toyota", "Camry", 2022, 15000, "Premium",
            VehicleCondition.USED, 95000.00, "Reliable sedan, well-maintained.", true));
        addTestVehicle(new Vehicle("VIN002", "Honda", "Civic", 2023, 500, "Sport",
            VehicleCondition.NEW, 115000.00, "Brand new Civic Sport.", true));
        addTestVehicle(new Vehicle("VIN003", "Ford", "Mustang", 2021, 25000, "GT",
            VehicleCondition.USED, 180000.00, "Iconic muscle car.", true));
        addTestVehicle(new Vehicle("VIN004", "BMW", "X5", 2022, 12000, "M Sport",
            VehicleCondition.USED, 250000.00, "Luxury SUV.", false)); // Not available
        addTestVehicle(new Vehicle("VIN005", "Audi", "A4", 2023, 100, "S Line",
            VehicleCondition.NEW, 160000.00, "New Audi A4.", true));
        addTestVehicle(new Vehicle("VIN006", "Mercedes", "C-Class", 2020, 45000, "AMG Line",
            VehicleCondition.USED, 155000.00, "Elegant and sporty.", true));
    }

    /**
     * Helper method to add a test vehicle to the storage
     * @param vehicle Vehicle to add
     */
    private void addTestVehicle(Vehicle vehicle) {
        vehicles.put(vehicle.getVin(), vehicle);
    }
    // !! END TEST DATA FOR VEHICLES !!

    /**
     * Retrieves all available vehicles from the in-memory storage.
     *
     * @return List of available Vehicle objects, empty list if none available
     */
    @Override
    public List<Vehicle> findAllAvailable() {
        return vehicles.values().stream()
                .filter(Vehicle::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Finds a vehicle by its VIN (Vehicle Identification Number).
     *
     * @param vin The VIN to search for
     * @return Optional containing the found Vehicle, or empty Optional if not found
     */
    @Override
    public Optional<Vehicle> findByVin(String vin) {
        return Optional.ofNullable(vehicles.get(vin));
    }

    // TODO: Implement save, update, delete methods as needed
}