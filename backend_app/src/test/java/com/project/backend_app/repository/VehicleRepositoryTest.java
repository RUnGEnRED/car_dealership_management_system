package com.project.backend_app.repository;

import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.entity.enums.VehicleCondition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link VehicleRepository}.
 * Uses @DataJpaTest which configures an in-memory database (H2 by default)
 * and scans for @Entity classes and Spring Data JPA repositories.
 */
@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle1, vehicle2, vehicle3;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll(); // Ensure a clean state

        vehicle1 = new Vehicle(null, "VIN001", "Toyota", "Camry", 2022, new BigDecimal("25000.00"),
                VehicleCondition.NEW, VehicleAvailability.AVAILABLE, null, null, true);
        vehicle2 = new Vehicle(null, "VIN002", "Honda", "Civic", 2021, new BigDecimal("22000.00"),
                VehicleCondition.USED, VehicleAvailability.RESERVED, null, null, true);
        vehicle3 = new Vehicle(null, "VIN003", "Ford", "F-150", 2023, new BigDecimal("45000.00"),
                VehicleCondition.NEW, VehicleAvailability.AVAILABLE, null, null, false); // Inactive

        vehicleRepository.saveAll(List.of(vehicle1, vehicle2, vehicle3));
    }

    @AfterEach
    void tearDown() {
        // Clean up database after each test if not relying solely on @DataJpaTest rollback.
        vehicleRepository.deleteAll();
    }


    @Test
    void whenFindByVin_withExistingVin_thenReturnVehicle() {
        Optional<Vehicle> found = vehicleRepository.findByVin("VIN001");
        assertThat(found).isPresent();
        assertThat(found.get().getMake()).isEqualTo("Toyota");
    }

    @Test
    void whenFindByVin_withNonExistingVin_thenReturnEmpty() {
        Optional<Vehicle> found = vehicleRepository.findByVin("NONEXISTENTVIN");
        assertThat(found).isNotPresent();
    }

    @Test
    void whenFindAllByActiveTrue_thenReturnOnlyActiveVehicles() {
        List<Vehicle> activeVehicles = vehicleRepository.findAllByActiveTrue();
        assertThat(activeVehicles).hasSize(2); // vehicle1 and vehicle2 are active
        assertThat(activeVehicles).extracting(Vehicle::getVin).containsExactlyInAnyOrder("VIN001", "VIN002");
        assertThat(activeVehicles).extracting(Vehicle::isActive).containsOnly(true);
    }

    @Test
    void whenFindAllByAvailabilityAndActiveTrue_thenReturnCorrectVehicles() {
        // vehicle1 is AVAILABLE and active
        List<Vehicle> availableAndActive = vehicleRepository.findAllByAvailabilityAndActiveTrue(VehicleAvailability.AVAILABLE);
        assertThat(availableAndActive).hasSize(1);
        assertThat(availableAndActive.get(0).getVin()).isEqualTo("VIN001");

        // vehicle2 is RESERVED and active
        List<Vehicle> reservedAndActive = vehicleRepository.findAllByAvailabilityAndActiveTrue(VehicleAvailability.RESERVED);
        assertThat(reservedAndActive).hasSize(1);
        assertThat(reservedAndActive.get(0).getVin()).isEqualTo("VIN002");

        // No SOLD and active vehicles in setup
        List<Vehicle> soldAndActive = vehicleRepository.findAllByAvailabilityAndActiveTrue(VehicleAvailability.SOLD);
        assertThat(soldAndActive).isEmpty();
    }

    @Test
    void whenExistsByVin_withExistingVin_thenReturnTrue() {
        boolean exists = vehicleRepository.existsByVin("VIN001");
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByVin_withNonExistingVin_thenReturnFalse() {
        boolean exists = vehicleRepository.existsByVin("NONEXISTENTVIN");
        assertThat(exists).isFalse();
    }

    @Test
    void whenSaveVehicle_thenIdIsGeneratedAndVehicleCanBeFound() {
        Vehicle newVehicle = new Vehicle(null, "VIN004", "Tesla", "Model S", 2023, new BigDecimal("80000.00"),
                VehicleCondition.NEW, VehicleAvailability.AVAILABLE, null, null, true);

        Vehicle savedVehicle = vehicleRepository.save(newVehicle);

        assertThat(savedVehicle.getId()).isNotNull();
        Optional<Vehicle> found = vehicleRepository.findById(savedVehicle.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getVin()).isEqualTo("VIN004");
    }
}