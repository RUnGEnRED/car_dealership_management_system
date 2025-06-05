package com.project.backend_app.repository;

import com.project.backend_app.entity.*;
import com.project.backend_app.entity.enums.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link RequestRepository}.
 */
@DataJpaTest
class RequestRepositoryTest {

    @Autowired private RequestRepository requestRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private VehicleRepository vehicleRepository;

    private Customer customer1;
    private Employee employee1;
    private Vehicle vehicle1;
    private Request request1, request2;

    @BeforeEach
    void setUp() {
        // Clear the database
        requestRepository.deleteAll();
        vehicleRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();


        customer1 = new Customer();
        customer1.setUsername("custReqTest");
        customer1.setEmail("custReqTest@example.com");
        customer1.setPassword("pass");
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setAddress(new Address("123 Main St", "TestCity", "12345", "TestCountry"));
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_CUSTOMER");
        customer1.setRoles(roles);
        customer1.setActive(true);
        customerRepository.save(customer1);

        employee1 = new Employee();
        employee1.setUsername("empReqTest");
        employee1.setEmail("empReqTest@example.com");
        employee1.setPassword("pass");
        employee1.setPosition(Position.EMPLOYEE);
        employee1.setFirstName("Jane");
        employee1.setLastName("Doe");
        employee1.setAddress(new Address("123 Main St", "TestCity", "12345", "TestCountry"));
        Set<String> empRoles = new HashSet<>();
        empRoles.add("ROLE_EMPLOYEE");
        employee1.setRoles(empRoles);
        employee1.setActive(true);
        employeeRepository.save(employee1);

        vehicle1 = new Vehicle(null, "VINREQ001", "Make", "Model", 2020, BigDecimal.TEN,
                VehicleCondition.USED, VehicleAvailability.AVAILABLE, null, null, true);
        vehicleRepository.save(vehicle1);

        request1 = new Request();
        request1.setCustomer(customer1);
        request1.setVehicle(vehicle1);
        request1.setRequestType(RequestType.PURCHASE);
        request1.setStatus(RequestStatus.PENDING);
        request1.setCustomerNotes("I want to buy this car.");

        request2 = new Request();
        request2.setCustomer(customer1); // Same customer, different request
        request2.setVehicle(vehicle1);   // Same vehicle
        request2.setRequestType(RequestType.INSPECTION);
        request2.setStatus(RequestStatus.ACCEPTED);
        request2.setAssignedEmployee(employee1);
        request2.setCustomerNotes("Please inspect brakes.");

        requestRepository.saveAll(List.of(request1, request2));
    }

    @AfterEach
    void tearDown() {
        requestRepository.deleteAll();
        vehicleRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void whenFindAllByCustomer_thenReturnCustomerRequests() {
        List<Request> requests = requestRepository.findAllByCustomer(customer1);
        assertThat(requests).hasSize(2);
        assertThat(requests).extracting(Request::getRequestType).containsExactlyInAnyOrder(RequestType.PURCHASE, RequestType.INSPECTION);
    }

    @Test
    void whenFindAllByAssignedEmployee_thenReturnEmployeeRequests() {
        List<Request> requests = requestRepository.findAllByAssignedEmployee(employee1);
        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getRequestType()).isEqualTo(RequestType.INSPECTION);
    }

    @Test
    void whenFindAllByVehicle_thenReturnVehicleRequests() {
        List<Request> requests = requestRepository.findAllByVehicle(vehicle1);
        assertThat(requests).hasSize(2);
    }

    @Test
    void whenFindAllByStatus_withPending_thenReturnPendingRequests() {
        List<Request> requests = requestRepository.findAllByStatus(RequestStatus.PENDING);
        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getRequestType()).isEqualTo(RequestType.PURCHASE);
    }

    @Test
    void whenFindAllByRequestType_withPurchase_thenReturnPurchaseRequests() {
        List<Request> requests = requestRepository.findAllByRequestType(RequestType.PURCHASE);
        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getStatus()).isEqualTo(RequestStatus.PENDING);
    }
}