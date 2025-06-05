package com.project.backend_app.service.impl;

import com.project.backend_app.dto.request.CreatePurchaseRequest;
import com.project.backend_app.dto.request.CreateServiceRequest;
import com.project.backend_app.dto.request.RequestDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RequestServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock private RequestRepository requestRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private VehicleRepository vehicleRepository;
    @Mock private EmployeeRepository employeeRepository;

    @InjectMocks private RequestServiceImpl requestService;

    private Customer customer;
    private Vehicle availableVehicle;
    private Vehicle soldVehicle;
    private Employee employee;
    private Request pendingPurchaseRequest;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setUsername("custuser");

        availableVehicle = new Vehicle();
        availableVehicle.setId(10L);
        availableVehicle.setVin("AVAILABLEVIN");
        availableVehicle.setActive(true);
        availableVehicle.setAvailability(VehicleAvailability.AVAILABLE);

        soldVehicle = new Vehicle();
        soldVehicle.setId(11L);
        soldVehicle.setVin("SOLDVIN");
        soldVehicle.setActive(true);
        soldVehicle.setAvailability(VehicleAvailability.SOLD);
        soldVehicle.setOwner(customer);


        employee = new Employee();
        employee.setId(20L);
        employee.setUsername("empuser");

        pendingPurchaseRequest = new Request();
        pendingPurchaseRequest.setId(100L);
        pendingPurchaseRequest.setCustomer(customer);
        pendingPurchaseRequest.setVehicle(availableVehicle);
        pendingPurchaseRequest.setRequestType(RequestType.PURCHASE);
        pendingPurchaseRequest.setStatus(RequestStatus.PENDING);
    }

    // --- createPurchaseRequest Tests ---
    @Test
    void whenCreatePurchaseRequest_withAvailableVehicle_thenSuccessAndVehicleReserved() {
        CreatePurchaseRequest createDto = new CreatePurchaseRequest(availableVehicle.getId(), "Notes");
        when(customerRepository.findByUsername("custuser")).thenReturn(Optional.of(customer));
        when(vehicleRepository.findById(availableVehicle.getId())).thenReturn(Optional.of(availableVehicle));
        when(requestRepository.save(any(Request.class))).thenAnswer(inv -> {
            Request r = inv.getArgument(0);
            r.setId(101L); return r;
        });
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(availableVehicle);


        RequestDto result = requestService.createPurchaseRequest(createDto, "custuser");

        assertThat(result).isNotNull();
        assertThat(result.getRequestType()).isEqualTo(RequestType.PURCHASE);
        assertThat(result.getStatus()).isEqualTo(RequestStatus.PENDING);
        assertThat(availableVehicle.getAvailability()).isEqualTo(VehicleAvailability.RESERVED);
        verify(vehicleRepository).save(availableVehicle);
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void whenCreatePurchaseRequest_withVehicleNotAvailable_thenThrowRuntimeException() {
        CreatePurchaseRequest createDto = new CreatePurchaseRequest(soldVehicle.getId(), "Notes");
        when(customerRepository.findByUsername("custuser")).thenReturn(Optional.of(customer));
        when(vehicleRepository.findById(soldVehicle.getId())).thenReturn(Optional.of(soldVehicle)); // soldVehicle is SOLD

        assertThatThrownBy(() -> requestService.createPurchaseRequest(createDto, "custuser"))
                .isInstanceOf(RuntimeException.class) // Or BadRequestException
                .hasMessageContaining("not available for purchase");
        verify(requestRepository, never()).save(any(Request.class));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    // --- createServiceRequest Tests ---
    @Test
    void whenCreateServiceRequest_forOwnedVehicle_thenSuccess() {
        // Ensure availableVehicle is owned by customer for this test
        availableVehicle.setOwner(customer);
        CreateServiceRequest createDto = new CreateServiceRequest(availableVehicle.getId(), "Service needed");
        when(customerRepository.findByUsername("custuser")).thenReturn(Optional.of(customer));
        when(vehicleRepository.findById(availableVehicle.getId())).thenReturn(Optional.of(availableVehicle));
        when(requestRepository.save(any(Request.class))).thenAnswer(inv -> {
            Request r = inv.getArgument(0);
            r.setId(102L); return r;
        });

        RequestDto result = requestService.createServiceRequest(createDto, "custuser");
        assertThat(result).isNotNull();
        assertThat(result.getRequestType()).isEqualTo(RequestType.SERVICE);
    }

    @Test
    void whenCreateServiceRequest_forNotOwnedVehicle_thenThrowRuntimeException() {
        CreateServiceRequest createDto = new CreateServiceRequest(availableVehicle.getId(), "Service needed");
        // availableVehicle.owner is null in setup or different customer
        availableVehicle.setOwner(null);
        when(customerRepository.findByUsername("custuser")).thenReturn(Optional.of(customer));
        when(vehicleRepository.findById(availableVehicle.getId())).thenReturn(Optional.of(availableVehicle));

        assertThatThrownBy(() -> requestService.createServiceRequest(createDto, "custuser"))
                .isInstanceOf(RuntimeException.class) // Or BadRequestException
                .hasMessageContaining("not owned by customer");
    }

    // --- acceptRequest Tests ---
    @Test
    void whenAcceptPurchaseRequest_thenStatusAcceptedVehicleSoldAndOwned() {
        when(requestRepository.findById(pendingPurchaseRequest.getId())).thenReturn(Optional.of(pendingPurchaseRequest));
        when(employeeRepository.findByUsername("empuser")).thenReturn(Optional.of(employee));
        // Ensure vehicle is RESERVED before accepting
        availableVehicle.setAvailability(VehicleAvailability.RESERVED);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(availableVehicle);
        when(requestRepository.save(any(Request.class))).thenReturn(pendingPurchaseRequest);


        RequestDto result = requestService.acceptRequest(pendingPurchaseRequest.getId(), "empuser");

        assertThat(result.getStatus()).isEqualTo(RequestStatus.ACCEPTED);
        assertThat(result.getAssignedEmployeeUsername()).isEqualTo("empuser");
        assertThat(availableVehicle.getAvailability()).isEqualTo(VehicleAvailability.SOLD);
        assertThat(availableVehicle.getOwner()).isEqualTo(customer);
        verify(vehicleRepository).save(availableVehicle);
        verify(requestRepository).save(pendingPurchaseRequest);
    }

    @Test
    void whenAcceptPurchaseRequest_vehicleNotReserved_thenThrowRuntimeException() {
        pendingPurchaseRequest.getVehicle().setAvailability(VehicleAvailability.AVAILABLE); // Not RESERVED
        when(requestRepository.findById(pendingPurchaseRequest.getId())).thenReturn(Optional.of(pendingPurchaseRequest));
        when(employeeRepository.findByUsername("empuser")).thenReturn(Optional.of(employee));

        assertThatThrownBy(() -> requestService.acceptRequest(pendingPurchaseRequest.getId(), "empuser"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no longer RESERVED");
    }


    // --- rejectRequest Tests ---
    @Test
    void whenRejectPurchaseRequest_thenStatusRejectedVehicleAvailable() {
        // Setup: vehicle was reserved due to this request
        availableVehicle.setAvailability(VehicleAvailability.RESERVED);
        pendingPurchaseRequest.setVehicle(availableVehicle);

        when(requestRepository.findById(pendingPurchaseRequest.getId())).thenReturn(Optional.of(pendingPurchaseRequest));
        when(employeeRepository.findByUsername("empuser")).thenReturn(Optional.of(employee));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(availableVehicle);
        when(requestRepository.save(any(Request.class))).thenReturn(pendingPurchaseRequest);

        RequestDto result = requestService.rejectRequest(pendingPurchaseRequest.getId(), "empuser");

        assertThat(result.getStatus()).isEqualTo(RequestStatus.REJECTED);
        assertThat(result.getAssignedEmployeeUsername()).isEqualTo("empuser");
        assertThat(availableVehicle.getAvailability()).isEqualTo(VehicleAvailability.AVAILABLE);
        verify(vehicleRepository).save(availableVehicle);
        verify(requestRepository).save(pendingPurchaseRequest);
    }

    @Test
    void whenRejectRequest_notPending_thenThrowRuntimeException() {
        pendingPurchaseRequest.setStatus(RequestStatus.ACCEPTED); // Not PENDING
        when(requestRepository.findById(pendingPurchaseRequest.getId())).thenReturn(Optional.of(pendingPurchaseRequest));

        assertThatThrownBy(() -> requestService.rejectRequest(pendingPurchaseRequest.getId(), "empuser"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not in PENDING state");
    }


    // Might add more tests for:
    // - createInspectionRequest
    // - getRequestById (found, not found)
    // - getAllRequests
    // - getRequestsByCustomerId / getRequestsByCustomerUsername (found, customer not found, no requests)
    // - Edge cases for accept/reject (request not found, employee not found, request not pending)
}