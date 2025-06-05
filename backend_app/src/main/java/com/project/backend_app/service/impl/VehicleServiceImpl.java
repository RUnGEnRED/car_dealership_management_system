package com.project.backend_app.service.impl;

import com.project.backend_app.dto.vehicle.CreateVehicleRequest;
import com.project.backend_app.dto.vehicle.UpdateVehicleRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.repository.VehicleRepository;
import com.project.backend_app.service.VehicleService;
import com.project.backend_app.service.mapper.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the VehicleService interface.
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public VehicleDto createVehicle(CreateVehicleRequest createRequest) {
        if (vehicleRepository.existsByVin(createRequest.getVin())) {
            throw new RuntimeException("Error: VIN " + createRequest.getVin() + " is already in use!");
        }
        Vehicle vehicle = EntityDtoMapper.toVehicleEntity(createRequest);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return EntityDtoMapper.toVehicleDto(savedVehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public VehicleDto getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        if (!vehicle.isActive()) {
            throw new RuntimeException("Vehicle not found with id: " + id + " (it is inactive)");
        }
        return EntityDtoMapper.toVehicleDto(vehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> getAllActiveVehicles() {
        return vehicleRepository.findAllByActiveTrue().stream()
                .map(EntityDtoMapper::toVehicleDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> getAllActiveAvailableVehicles() {
        return vehicleRepository.findAllByAvailabilityAndActiveTrue(VehicleAvailability.AVAILABLE).stream()
                .map(EntityDtoMapper::toVehicleDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public VehicleDto updateVehicle(Long id, UpdateVehicleRequest updateRequest) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        // VIN is typically not updated. If needed, add logic here.

        if (updateRequest.getMake() != null) {
            vehicle.setMake(updateRequest.getMake());
        }
        if (updateRequest.getModel() != null) {
            vehicle.setModel(updateRequest.getModel());
        }
        if (updateRequest.getProductionYear() != null) {
            vehicle.setProductionYear(updateRequest.getProductionYear());
        }
        if (updateRequest.getPrice() != null) {
            vehicle.setPrice(updateRequest.getPrice());
        }
        if (updateRequest.getVehicleCondition() != null) {
            vehicle.setVehicleCondition(updateRequest.getVehicleCondition());
        }
        if (updateRequest.getAvailability() != null) {
            vehicle.setAvailability(updateRequest.getAvailability());
        }
        if (updateRequest.getActive() != null) {
            vehicle.setActive(updateRequest.getActive());
        }

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return EntityDtoMapper.toVehicleDto(updatedVehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deactivateVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void activateVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        vehicle.setActive(true);
        vehicleRepository.save(vehicle);
    }
}