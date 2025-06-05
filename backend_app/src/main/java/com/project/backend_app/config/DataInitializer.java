package com.project.backend_app.config;

import com.project.backend_app.entity.*;
import com.project.backend_app.entity.enums.*;
import com.project.backend_app.repository.*;
import com.project.backend_app.service.impl.AuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Initializes the database with some sample data on application startup.
 * This is useful for development and demonstration purposes.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final RequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(EmployeeRepository employeeRepository,
                           CustomerRepository customerRepository,
                           VehicleRepository vehicleRepository,
                           RequestRepository requestRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.requestRepository = requestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Callback used to run the bean.
     * This method will be executed on application startup.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    @Transactional // Ensures all operations are part of a single transaction
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization...");

        if (employeeRepository.count() == 0 && customerRepository.count() == 0) {
            logger.info("No existing users found. Proceeding with data initialization.");
            createEmployees();
            createCustomers();
            createVehiclesAndRequests();
            logger.info("Data initialization completed.");
        } else {
            logger.info("Data already exists. Skipping initialization.");
        }
    }

    private void createEmployees() {
        Address commonAddress = new Address("10 Corporate Dr", "Office Park", "OP500", "Workland");

        // 1 Manager
        Employee manager = new Employee();
        manager.setFirstName("Alice");
        manager.setLastName("Manager");
        manager.setEmail("alice.manager@showroom.com");
        manager.setUsername("alicemgr");
        manager.setPassword(passwordEncoder.encode("password123"));
        manager.setPhoneNumber("555-0101");
        manager.setAddress(commonAddress);
        manager.setPosition(Position.MANAGER);
        Set<String> managerRoles = new HashSet<>(Arrays.asList(AuthServiceImpl.ROLE_MANAGER, AuthServiceImpl.ROLE_EMPLOYEE));
        manager.setRoles(managerRoles);
        manager.setActive(true);
        employeeRepository.save(manager);

        // 2 Employees
        Employee emp1 = new Employee();
        emp1.setFirstName("Bob");
        emp1.setLastName("Worker");
        emp1.setEmail("bob.worker@showroom.com");
        emp1.setUsername("bobworker");
        emp1.setPassword(passwordEncoder.encode("password123"));
        emp1.setPhoneNumber("555-0102");
        emp1.setAddress(commonAddress);
        emp1.setPosition(Position.EMPLOYEE);
        Set<String> empRoles = new HashSet<>(List.of(AuthServiceImpl.ROLE_EMPLOYEE));
        emp1.setRoles(empRoles);
        emp1.setActive(true);
        employeeRepository.save(emp1);

        Employee emp2 = new Employee();
        emp2.setFirstName("Charlie");
        emp2.setLastName("Sales");
        emp2.setEmail("charlie.sales@showroom.com");
        emp2.setUsername("charliesales");
        emp2.setPassword(passwordEncoder.encode("password123"));
        emp2.setPhoneNumber("555-0103");
        emp2.setAddress(new Address("20 Retail Row", "Office Park", "OP501", "Workland"));
        emp2.setPosition(Position.EMPLOYEE);
        emp2.setRoles(empRoles);
        emp2.setActive(true);
        employeeRepository.save(emp2);

        logger.info("Created {} employees.", employeeRepository.count());
    }

    private void createCustomers() {
        // 3 Customers
        Customer cust1 = new Customer();
        cust1.setFirstName("David");
        cust1.setLastName("Client");
        cust1.setEmail("david.client@example.com");
        cust1.setUsername("davidclient");
        cust1.setPassword(passwordEncoder.encode("password123"));
        cust1.setPhoneNumber("555-0201");
        cust1.setAddress(new Address("1 Customer Lane", "Suburbia", "CS100", "Clientville"));
        Set<String> custRoles = new HashSet<>(List.of(AuthServiceImpl.ROLE_CUSTOMER));
        cust1.setRoles(custRoles);
        cust1.setActive(true);
        customerRepository.save(cust1);

        Customer cust2 = new Customer();
        cust2.setFirstName("Eve");
        cust2.setLastName("Shopper");
        cust2.setEmail("eve.shopper@example.com");
        cust2.setUsername("eveshopper");
        cust2.setPassword(passwordEncoder.encode("password123"));
        cust2.setPhoneNumber("555-0202");
        cust2.setAddress(new Address("2 Buyer Ave", "Marketown", "BT200", "Clientville"));
        cust2.setRoles(custRoles);
        cust2.setActive(true);
        customerRepository.save(cust2);

        Customer cust3 = new Customer();
        cust3.setFirstName("Frank");
        cust3.setLastName("Looker");
        cust3.setEmail("frank.looker@example.com");
        cust3.setUsername("franklooker");
        cust3.setPassword(passwordEncoder.encode("password123"));
        cust3.setPhoneNumber("555-0203");
        cust3.setAddress(new Address("3 Viewer Rd", "Viewpoint", "VP300", "Clientville"));
        cust3.setRoles(custRoles);
        cust3.setActive(true);
        customerRepository.save(cust3);

        logger.info("Created {} customers.", customerRepository.count());
    }

    private void createVehiclesAndRequests() {
        // Retrieve created users to associate with vehicles and requests
        Customer david = customerRepository.findByUsername("davidclient").orElseThrow();
        Customer eve = customerRepository.findByUsername("eveshopper").orElseThrow();
        Customer frank = customerRepository.findByUsername("franklooker").orElseThrow();

        Employee alice = employeeRepository.findByUsername("alicemgr").orElseThrow();
        Employee bob = employeeRepository.findByUsername("bobworker").orElseThrow();

        // 5 Vehicles
        Vehicle v1_soldToDavid = new Vehicle(null, "SOLDVIN0000000001", "Toyota", "Camry", 2021, new BigDecimal("22000.00"),
                VehicleCondition.USED, VehicleAvailability.SOLD, david, null, true);
        vehicleRepository.save(v1_soldToDavid);

        Vehicle v2_soldToEve = new Vehicle(null, "SOLDVIN0000000002", "Honda", "Civic", 2022, new BigDecimal("24000.00"),
                VehicleCondition.NEW, VehicleAvailability.SOLD, eve, null, true);
        vehicleRepository.save(v2_soldToEve);

        Vehicle v3_reservedByFrank = new Vehicle(null, "RESERVEDVIN000003", "Ford", "Focus", 2023, new BigDecimal("26000.00"),
                VehicleCondition.NEW, VehicleAvailability.RESERVED, null, null, true);
        vehicleRepository.save(v3_reservedByFrank);

        Vehicle v4_available1 = new Vehicle(null, "AVAILVIN000000004", "Mazda", "3", 2023, new BigDecimal("27000.00"),
                VehicleCondition.NEW, VehicleAvailability.AVAILABLE, null, null, true);
        vehicleRepository.save(v4_available1);

        Vehicle v5_available2 = new Vehicle(null, "AVAILVIN000000005", "Nissan", "Sentra", 2020, new BigDecimal("18000.00"),
                VehicleCondition.USED, VehicleAvailability.AVAILABLE, null, null, true);
        vehicleRepository.save(v5_available2);

        logger.info("Created {} vehicles.", vehicleRepository.count());

        // 8 Requests
        // Request 1: David bought v1 (Accepted Purchase)
        Request req1 = new Request(null, david, v1_soldToDavid, alice, RequestType.PURCHASE,
                RequestStatus.ACCEPTED, "Purchase approved.", null, null);
        requestRepository.save(req1);

        // Request 2: Eve bought v2 (Accepted Purchase)
        Request req2 = new Request(null, eve, v2_soldToEve, alice, RequestType.PURCHASE,
                RequestStatus.ACCEPTED, "Eve's purchase.", null, null);
        requestRepository.save(req2);

        // Request 3: Frank's request for v3 (Pending Purchase, vehicle is RESERVED)
        Request req3 = new Request(null, frank, v3_reservedByFrank, null, RequestType.PURCHASE,
                RequestStatus.PENDING, "Frank wants to buy the Focus.", null, null);
        requestRepository.save(req3);

        // Request 4: David's service request for his v1 (Accepted Inspection/Service)
        Request req4 = new Request(null, david, v1_soldToDavid, bob, RequestType.INSPECTION, // Or SERVICE
                RequestStatus.ACCEPTED, "Oil change and tire rotation.", null, null);
        requestRepository.save(req4);

        // Request 5: Frank's another PENDING purchase request for v4_available1
        Request req5 = new Request(null, frank, v4_available1, null, RequestType.PURCHASE,
                RequestStatus.PENDING, "Frank is also interested in Mazda 3.", null, null);
        requestRepository.save(req5);

        // Request 6: Eve's REJECTED service request for v5_available2 (a car she doesn't own)
        Request req6 = new Request(null, eve, v5_available2, bob, RequestType.PURCHASE,
                RequestStatus.REJECTED, "Credit check failed.", null, null);
        requestRepository.save(req6);

        // Request 7: David's PENDING inspection request for v1_soldToDavid
        Request req7 = new Request(null, david, v1_soldToDavid, null, RequestType.INSPECTION,
                RequestStatus.PENDING, "Check engine light came on.", null, null);
        requestRepository.save(req7);

        // Request 8: A PENDING service request from Frank for v3_reservedByFrank (even if purchase is pending)
        Request req8 = new Request(null, frank, v3_reservedByFrank, null, RequestType.SERVICE,
                RequestStatus.PENDING, "Wants to install custom spoiler before finalizing purchase.", null, null);
        requestRepository.save(req8);


        logger.info("Created {} requests.", requestRepository.count());
    }
}