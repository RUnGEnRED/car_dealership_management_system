package com.project.backend_app.service.impl;

import com.project.backend_app.dto.request.*;
import com.project.backend_app.entity.Customer;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.Request;
import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.entity.enums.RequestStatus;
import com.project.backend_app.entity.enums.RequestType;
import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.repository.CustomerRepository;
import com.project.backend_app.repository.EmployeeRepository;
import com.project.backend_app.repository.RequestRepository;
import com.project.backend_app.repository.VehicleRepository;
import com.project.backend_app.service.RequestService;
import com.project.backend_app.service.mapper.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the RequestService interface.
 */
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,
                              CustomerRepository customerRepository,
                              VehicleRepository vehicleRepository,
                              EmployeeRepository employeeRepository) {
        this.requestRepository = requestRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RequestDto createPurchaseRequest(CreatePurchaseRequest createRequest, String customerUsername) {
        Customer customer = customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found with username: " + customerUsername));

        Vehicle vehicle = vehicleRepository.findById(createRequest.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + createRequest.getVehicleId()));

        if (!vehicle.isActive() || vehicle.getAvailability() != VehicleAvailability.AVAILABLE) {
            throw new RuntimeException("Vehicle " + vehicle.getVin() + " is not available for purchase.");
        }

        Request newRequest = new Request();
        newRequest.setCustomer(customer);
        newRequest.setVehicle(vehicle);
        newRequest.setRequestType(RequestType.PURCHASE);
        newRequest.setStatus(RequestStatus.PENDING);
        newRequest.setCustomerNotes(createRequest.getCustomerNotes());

        // Reserve the vehicle
        vehicle.setAvailability(VehicleAvailability.RESERVED);
        vehicleRepository.save(vehicle);

        Request savedRequest = requestRepository.save(newRequest);
        return EntityDtoMapper.toRequestDto(savedRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RequestDto createServiceRequest(CreateServiceRequest createRequest, String customerUsername) {
        Customer customer = customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found with username: " + customerUsername));

        Vehicle vehicle = vehicleRepository.findById(createRequest.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + createRequest.getVehicleId()));

        // Check if the vehicle is owned by the customer making the request
        if (vehicle.getOwner() == null || !vehicle.getOwner().getId().equals(customer.getId())) {
            throw new RuntimeException("Vehicle " + vehicle.getVin() + " is not owned by customer " + customerUsername);
        }

        Request newRequest = new Request();
        newRequest.setCustomer(customer);
        newRequest.setVehicle(vehicle);
        newRequest.setRequestType(RequestType.SERVICE);
        newRequest.setStatus(RequestStatus.PENDING);
        newRequest.setCustomerNotes(createRequest.getCustomerNotes());

        Request savedRequest = requestRepository.save(newRequest);
        return EntityDtoMapper.toRequestDto(savedRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RequestDto createInspectionRequest(CreateInspectionRequest createRequest, String customerUsername) {
        Customer customer = customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found with username: " + customerUsername));

        Vehicle vehicle = vehicleRepository.findById(createRequest.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + createRequest.getVehicleId()));

        // Check if the vehicle is owned by the customer making the request
//        if (vehicle.getOwner() == null || !vehicle.getOwner().getId().equals(customer.getId())) {
//            throw new RuntimeException("Vehicle " + vehicle.getVin() + " is not owned by customer " + customerUsername);
//        }

        Request newRequest = new Request();
        newRequest.setCustomer(customer);
        newRequest.setVehicle(vehicle);
        newRequest.setRequestType(RequestType.INSPECTION);
        newRequest.setStatus(RequestStatus.PENDING);
        newRequest.setCustomerNotes(createRequest.getCustomerNotes());

        Request savedRequest = requestRepository.save(newRequest);
        return EntityDtoMapper.toRequestDto(savedRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public RequestDto getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        return EntityDtoMapper.toRequestDto(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getAllRequests() {
        return EntityDtoMapper.toRequestDtoList(requestRepository.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return EntityDtoMapper.toRequestDtoList(requestRepository.findAllByCustomer(customer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByCustomerUsername(String customerUsername) {
        Customer customer = customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found with username: " + customerUsername));
        return EntityDtoMapper.toRequestDtoList(requestRepository.findAllByCustomer(customer));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RequestDto acceptRequest(Long requestId, String employeeUsername) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request " + requestId + " is not in PENDING state.");
        }

        Employee employee = employeeRepository.findByUsername(employeeUsername)
                .orElseThrow(() -> new RuntimeException("Employee not found with username: " + employeeUsername));

        request.setStatus(RequestStatus.ACCEPTED);
        request.setAssignedEmployee(employee);

        if (request.getRequestType() == RequestType.PURCHASE) {
            Vehicle vehicle = request.getVehicle();
            if (vehicle.getAvailability() != VehicleAvailability.RESERVED) {
                // This could happen if another process changed the vehicle status.
                // Optimistic locking on Vehicle would also help catch this.
                throw new RuntimeException("Vehicle " + vehicle.getVin() + " is no longer RESERVED. Current status: " + vehicle.getAvailability());
            }
            vehicle.setAvailability(VehicleAvailability.SOLD);
            vehicle.setOwner(request.getCustomer());
            vehicleRepository.save(vehicle);
        }

        Request updatedRequest = requestRepository.save(request);
        return EntityDtoMapper.toRequestDto(updatedRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RequestDto rejectRequest(Long requestId, String employeeUsername) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request " + requestId + " is not in PENDING state.");
        }

        Employee employee = employeeRepository.findByUsername(employeeUsername)
                .orElseThrow(() -> new RuntimeException("Employee not found with username: " + employeeUsername));

        request.setStatus(RequestStatus.REJECTED);
        request.setAssignedEmployee(employee);

        if (request.getRequestType() == RequestType.PURCHASE) {
            Vehicle vehicle = request.getVehicle();
            // Only change back to AVAILABLE if it was RESERVED by this request's process
            if (vehicle.getAvailability() == VehicleAvailability.RESERVED) {
                vehicle.setAvailability(VehicleAvailability.AVAILABLE);
                vehicleRepository.save(vehicle);
            }
        }

        Request updatedRequest = requestRepository.save(request);
        return EntityDtoMapper.toRequestDto(updatedRequest);
    }
}