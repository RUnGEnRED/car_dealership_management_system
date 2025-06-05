package com.project.backend_app.service;

import com.project.backend_app.dto.request.*;

import java.util.List;

/**
 * Service interface for managing customer requests.
 */
public interface RequestService {

    /**
     * Creates a new purchase request.
     *
     * @param createRequest DTO containing purchase request details.
     * @param customerUsername The username of the customer making the request.
     * @return DTO of the created request.
     * @throws RuntimeException if customer or vehicle not found, or vehicle not available.
     */
    RequestDto createPurchaseRequest(CreatePurchaseRequest createRequest, String customerUsername);

    /**
     * Creates a new service request.
     *
     * @param createRequest DTO containing service request details.
     * @param customerUsername The username of the customer making the request.
     * @return DTO of the created request.
     * @throws RuntimeException if customer or vehicle not found, or vehicle not owned by customer.
     */
    RequestDto createServiceRequest(CreateServiceRequest createRequest, String customerUsername);

    /**
     * Creates a new inspection request.
     *
     * @param createRequest DTO containing inspection request details.
     * @param customerUsername The username of the customer making the request.
     * @return DTO of the created request.
     * @throws RuntimeException if customer or vehicle not found.
     */
    RequestDto createInspectionRequest(CreateInspectionRequest createRequest, String customerUsername);

    /**
     * Retrieves a request by its ID.
     *
     * @param requestId The ID of the request.
     * @return DTO of the found request.
     * @throws RuntimeException if the request is not found.
     */
    RequestDto getRequestById(Long requestId);

    /**
     * Retrieves all requests.
     * (Typically for admin/employee use)
     *
     * @return A list of DTOs representing all requests.
     */
    List<RequestDto> getAllRequests();

    /**
     * Retrieves all requests made by a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of DTOs representing the customer's requests.
     */
    List<RequestDto> getRequestsByCustomerId(Long customerId);

    /**
     * Retrieves all requests made by a specific customer identified by username.
     *
     * @param customerUsername The username of the customer.
     * @return A list of DTOs representing the customer's requests.
     */
    List<RequestDto> getRequestsByCustomerUsername(String customerUsername);


    /**
     * Accepts a pending request.
     *
     * @param requestId The ID of the request to accept.
     * @param employeeUsername The username of the employee accepting the request.
     * @return DTO of the updated request.
     * @throws RuntimeException if request or employee not found, or request not pending.
     */
    RequestDto acceptRequest(Long requestId, String employeeUsername);

    /**
     * Rejects a pending request.
     *
     * @param requestId The ID of the request to reject.
     * @param employeeUsername The username of the employee rejecting the request.
     * @return DTO of the updated request.
     * @throws RuntimeException if request or employee not found, or request not pending.
     */
    RequestDto rejectRequest(Long requestId, String employeeUsername);
}