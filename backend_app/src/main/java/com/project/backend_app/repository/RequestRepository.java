package com.project.backend_app.repository;

import com.project.backend_app.entity.Customer;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.Request;
import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.entity.enums.RequestStatus;
import com.project.backend_app.entity.enums.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the {@link Request} entity.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Finds all requests made by a specific customer.
     *
     * @param customer The customer whose requests are to be found.
     * @return A list of requests made by the customer.
     */
    List<Request> findAllByCustomer(Customer customer);

    /**
     * Finds all requests assigned to or handled by a specific employee.
     *
     * @param employee The employee whose assigned requests are to be found.
     * @return A list of requests assigned to the employee.
     */
    List<Request> findAllByAssignedEmployee(Employee employee);

    /**
     * Finds all requests related to a specific vehicle.
     *
     * @param vehicle The vehicle to which the requests pertain.
     * @return A list of requests related to the vehicle.
     */
    List<Request> findAllByVehicle(Vehicle vehicle);

    /**
     * Finds all requests with a specific status.
     *
     * @param status The status to filter by.
     * @return A list of requests with the given status.
     */
    List<Request> findAllByStatus(RequestStatus status);

    /**
     * Finds all requests of a specific type.
     *
     * @param requestType The type of request to filter by.
     * @return A list of requests of the given type.
     */
    List<Request> findAllByRequestType(RequestType requestType);
}