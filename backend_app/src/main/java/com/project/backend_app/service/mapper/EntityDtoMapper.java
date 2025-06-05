package com.project.backend_app.service.mapper; // Or com.project.backend_app.util

import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.dto.auth.CustomerRegisterRequest;
import com.project.backend_app.dto.auth.EmployeeRegisterRequest;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.dto.request.RequestDto;
import com.project.backend_app.dto.vehicle.CreateVehicleRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.entity.*;
import com.project.backend_app.entity.enums.VehicleAvailability;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between DTOs and Entities.
 */
public class EntityDtoMapper {

    // Address Mappers
    public static AddressDto toAddressDto(Address address) {
        if (address == null) return null;
        return new AddressDto(address.getStreet(), address.getCity(), address.getPostalCode(), address.getCountry());
    }

    public static Address toAddressEntity(AddressDto dto) {
        if (dto == null) return null;
        return new Address(dto.getStreet(), dto.getCity(), dto.getPostalCode(), dto.getCountry());
    }

    // Customer Mappers
    public static CustomerDto toCustomerDto(Customer customer) {
        if (customer == null) return null;
        return new CustomerDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                toAddressDto(customer.getAddress()),
                customer.getUsername(),
                customer.isActive()
        );
    }

    public static Customer toCustomerEntity(CustomerRegisterRequest dto) {
        if (dto == null) return null;
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(toAddressEntity(dto.getAddress()));
        customer.setUsername(dto.getUsername());
        // Password and roles are set in the service
        return customer;
    }

    // Employee Mappers
    public static EmployeeDto toEmployeeDto(Employee employee) {
        if (employee == null) return null;
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                toAddressDto(employee.getAddress()),
                employee.getUsername(),
                employee.getPosition(),
                employee.isActive()
        );
    }

    public static Employee toEmployeeEntity(EmployeeRegisterRequest dto) {
        if (dto == null) return null;
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setAddress(toAddressEntity(dto.getAddress()));
        employee.setUsername(dto.getUsername());
        employee.setPosition(dto.getPosition());
        // Password and roles are set in the service
        return employee;
    }

    // Vehicle Mappers
    public static VehicleDto toVehicleDto(Vehicle vehicle) {
        if (vehicle == null) return null;
        return new VehicleDto(
                vehicle.getId(),
                vehicle.getVin(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getProductionYear(),
                vehicle.getPrice(),
                vehicle.getVehicleCondition(),
                vehicle.getAvailability(),
                vehicle.getOwner() != null ? vehicle.getOwner().getId() : null,
                vehicle.isActive()
        );
    }

    public static List<VehicleDto> toVehicleDtoList(List<Vehicle> vehicles) {
        if (vehicles == null) return Collections.emptyList();
        return vehicles.stream().map(EntityDtoMapper::toVehicleDto).collect(Collectors.toList());
    }

    public static Vehicle toVehicleEntity(CreateVehicleRequest dto) {
        if (dto == null) return null;
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(dto.getVin());
        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setProductionYear(dto.getProductionYear());
        vehicle.setPrice(dto.getPrice());
        vehicle.setVehicleCondition(dto.getVehicleCondition());
        vehicle.setAvailability(VehicleAvailability.AVAILABLE); // Default
        vehicle.setActive(true); // Default
        return vehicle;
    }

    // Request Mappers
    public static RequestDto toRequestDto(Request request) {
        if (request == null) return null;
        return new RequestDto(
                request.getId(),
                request.getCustomer() != null ? request.getCustomer().getId() : null,
                request.getCustomer() != null ? request.getCustomer().getUsername() : null,
                request.getVehicle() != null ? request.getVehicle().getId() : null,
                request.getVehicle() != null ? request.getVehicle().getVin() : null,
                request.getAssignedEmployee() != null ? request.getAssignedEmployee().getId() : null,
                request.getAssignedEmployee() != null ? request.getAssignedEmployee().getUsername() : null,
                request.getRequestType(),
                request.getStatus(),
                request.getCustomerNotes(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

    public static List<RequestDto> toRequestDtoList(List<Request> requests) {
        if (requests == null) return Collections.emptyList();
        return requests.stream().map(EntityDtoMapper::toRequestDto).collect(Collectors.toList());
    }
}