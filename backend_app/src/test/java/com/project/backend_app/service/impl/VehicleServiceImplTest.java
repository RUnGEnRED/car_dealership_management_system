package com.project.backend_app.service.impl;

import com.project.backend_app.dto.vehicle.CreateVehicleRequest;
import com.project.backend_app.dto.vehicle.UpdateVehicleRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.entity.enums.VehicleCondition;
import com.project.backend_app.repository.VehicleRepository;
import com.project.backend_app.service.mapper.EntityDtoMapper; // Assuming this is a static utility
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VehicleServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle1;
    private VehicleDto vehicleDto1;
    private CreateVehicleRequest createVehicleRequest;

    /**
     * Sets up common test data before each test.
     */
    @BeforeEach
    void setUp() {
        vehicle1 = new Vehicle();
        vehicle1.setId(1L);
        vehicle1.setVin("TESTVIN1234567890");
        vehicle1.setMake("TestMake");
        vehicle1.setModel("TestModel");
        vehicle1.setProductionYear(2022);
        vehicle1.setPrice(new BigDecimal("25000.00"));
        vehicle1.setVehicleCondition(VehicleCondition.NEW);
        vehicle1.setAvailability(VehicleAvailability.AVAILABLE);
        vehicle1.setActive(true);

        vehicleDto1 = EntityDtoMapper.toVehicleDto(vehicle1);

        createVehicleRequest = new CreateVehicleRequest(
                "NEWVIN12345678901",
                "NewMake",
                "NewModel",
                2023,
                new BigDecimal("30000.00"),
                VehicleCondition.NEW
        );
    }

    /**
     * Test for {@link VehicleServiceImpl#createVehicle(CreateVehicleRequest)} - successful creation.
     */
    @Test
    void whenCreateVehicle_withValidData_thenReturnCreatedVehicleDto() {
        // Given
        when(vehicleRepository.existsByVin(anyString())).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> {
            Vehicle saved = invocation.getArgument(0);
            saved.setId(2L); // Simulate ID generation on save
            return saved;
        });

        // When
        VehicleDto result = vehicleService.createVehicle(createVehicleRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getVin()).isEqualTo(createVehicleRequest.getVin());
        assertThat(result.getMake()).isEqualTo(createVehicleRequest.getMake());
        verify(vehicleRepository, times(1)).existsByVin(createVehicleRequest.getVin());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    /**
     * Test for {@link VehicleServiceImpl#createVehicle(CreateVehicleRequest)} - VIN already exists.
     */
    @Test
    void whenCreateVehicle_withExistingVin_thenThrowRuntimeException() {
        // Given
        when(vehicleRepository.existsByVin(createVehicleRequest.getVin())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> vehicleService.createVehicle(createVehicleRequest))
                .isInstanceOf(RuntimeException.class) // Or DuplicateResourceException if service is updated
                .hasMessageContaining("Error: VIN " + createVehicleRequest.getVin() + " is already in use!");

        verify(vehicleRepository, times(1)).existsByVin(createVehicleRequest.getVin());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    /**
     * Test for {@link VehicleServiceImpl#getVehicleById(Long)} - vehicle found and active.
     */
    @Test
    void whenGetVehicleById_withExistingAndActiveId_thenReturnVehicleDto() {
        // Given
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));

        // When
        VehicleDto result = vehicleService.getVehicleById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(vehicle1.getId());
        assertThat(result.getVin()).isEqualTo(vehicle1.getVin());
        verify(vehicleRepository, times(1)).findById(1L);
    }

    /**
     * Test for {@link VehicleServiceImpl#getVehicleById(Long)} - vehicle not found.
     */
    @Test
    void whenGetVehicleById_withNonExistingId_thenThrowRuntimeException() {
        // Given
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> vehicleService.getVehicleById(99L))
                .isInstanceOf(RuntimeException.class) // Or ResourceNotFoundException
                .hasMessageContaining("Vehicle not found with id: 99");
        verify(vehicleRepository, times(1)).findById(99L);
    }

    /**
     * Test for {@link VehicleServiceImpl#getVehicleById(Long)} - vehicle found but inactive.
     */
    @Test
    void whenGetVehicleById_withInactiveId_thenThrowRuntimeException() {
        // Given
        vehicle1.setActive(false);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));

        // When & Then
        assertThatThrownBy(() -> vehicleService.getVehicleById(1L))
                .isInstanceOf(RuntimeException.class) // Or specific exception
                .hasMessageContaining("Vehicle not found with id: 1 (it is inactive)");
        verify(vehicleRepository, times(1)).findById(1L);
    }

    /**
     * Test for {@link VehicleServiceImpl#getAllActiveVehicles()} - returns list of active vehicles.
     */
    @Test
    void whenGetAllActiveVehicles_thenReturnListOfVehicleDtos() {
        // Given
        Vehicle vehicle2 = new Vehicle(); // Another active vehicle
        vehicle2.setId(2L);
        vehicle2.setVin("ACTIVEVIN002");
        vehicle2.setActive(true);
        // ... can set other properties for vehicle2

        when(vehicleRepository.findAllByActiveTrue()).thenReturn(List.of(vehicle1, vehicle2));

        // When
        List<VehicleDto> results = vehicleService.getAllActiveVehicles();

        // Then
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getVin()).isEqualTo(vehicle1.getVin());
        verify(vehicleRepository, times(1)).findAllByActiveTrue();
    }

    /**
     * Test for {@link VehicleServiceImpl#getAllActiveVehicles()} - returns empty list when no active vehicles.
     */
    @Test
    void whenGetAllActiveVehicles_withNoActiveVehicles_thenReturnEmptyList() {
        // Given
        when(vehicleRepository.findAllByActiveTrue()).thenReturn(Collections.emptyList());

        // When
        List<VehicleDto> results = vehicleService.getAllActiveVehicles();

        // Then
        assertThat(results).isNotNull();
        assertThat(results).isEmpty();
        verify(vehicleRepository, times(1)).findAllByActiveTrue();
    }

    /**
     * Test for {@link VehicleServiceImpl#getAllActiveAvailableVehicles()} - returns list of active and available vehicles.
     */
    @Test
    void whenGetAllActiveAvailableVehicles_thenReturnListOfAvailableVehicleDtos() {
        // Given
        Vehicle vehicle2 = new Vehicle(); // Active but RESERVED vehicle
        vehicle2.setId(2L);
        vehicle2.setVin("RESERVEDVIN002");
        vehicle2.setActive(true);
        vehicle2.setAvailability(VehicleAvailability.RESERVED);
        // ... can set other properties for vehicle2

        // vehicle1 is active and available by default in setUp()

        when(vehicleRepository.findAllByAvailabilityAndActiveTrue(VehicleAvailability.AVAILABLE))
                .thenReturn(List.of(vehicle1));

        // When
        List<VehicleDto> results = vehicleService.getAllActiveAvailableVehicles();

        // Then
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getVin()).isEqualTo(vehicle1.getVin());
        assertThat(results.get(0).getAvailability()).isEqualTo(VehicleAvailability.AVAILABLE);
        verify(vehicleRepository, times(1)).findAllByAvailabilityAndActiveTrue(VehicleAvailability.AVAILABLE);
    }

    /**
     * Test for {@link VehicleServiceImpl#updateVehicle(Long, UpdateVehicleRequest)} - successful update.
     */
    @Test
    void whenUpdateVehicle_withExistingIdAndValidData_thenReturnUpdatedVehicleDto() {
        // Given
        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest();
        updateRequest.setMake("UpdatedMake");
        updateRequest.setPrice(new BigDecimal("26000.00"));
        updateRequest.setActive(true); // Assuming active status remains true

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VehicleDto result = vehicleService.updateVehicle(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMake()).isEqualTo("UpdatedMake");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("26000.00"));
        assertThat(vehicle1.getMake()).isEqualTo("UpdatedMake"); // Check if original entity was modified before save
        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(vehicle1);
    }

    /**
     * Test for {@link VehicleServiceImpl#updateVehicle(Long, UpdateVehicleRequest)} - vehicle not found.
     */
    @Test
    void whenUpdateVehicle_withNonExistingId_thenThrowRuntimeException() {
        // Given
        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest();
        updateRequest.setMake("UpdatedMake");
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> vehicleService.updateVehicle(99L, updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Vehicle not found with id: 99");
        verify(vehicleRepository, times(1)).findById(99L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    /**
     * Test for {@link VehicleServiceImpl#deactivateVehicle(Long)} - successful deactivation.
     */
    @Test
    void whenDeactivateVehicle_withExistingId_thenVehicleIsSetInactiveAndSaved() {
        // Given
        assertThat(vehicle1.isActive()).isTrue(); // Pre-condition
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle1);

        // When
        vehicleService.deactivateVehicle(1L);

        // Then
        assertThat(vehicle1.isActive()).isFalse(); // Check if the entity's state changed
        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(vehicle1);
    }

    /**
     * Test for {@link VehicleServiceImpl#activateVehicle(Long)} - successful activation.
     */
    @Test
    void whenActivateVehicle_withExistingId_thenVehicleIsSetToActiveAndSaved() {
        // Given
        vehicle1.setActive(false); // Start with an inactive vehicle
        assertThat(vehicle1.isActive()).isFalse(); // Pre-condition
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle1);

        // When
        vehicleService.activateVehicle(1L);

        // Then
        assertThat(vehicle1.isActive()).isTrue(); // Check if the entity's state changed
        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(vehicle1);
    }
}