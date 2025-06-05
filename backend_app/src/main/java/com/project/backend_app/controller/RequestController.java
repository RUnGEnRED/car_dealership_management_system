package com.project.backend_app.controller;

import com.project.backend_app.dto.request.*;
import com.project.backend_app.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling customer requests (purchase, service, inspection).
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/requests")
@Tag(name = "Requests", description = "Endpoints for managing customer requests")
@SecurityRequirement(name = "bearerAuth")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Creates a new vehicle purchase request.
     * Requires CUSTOMER role.
     *
     * @param createRequest DTO for purchase request.
     * @param authentication Current user's authentication.
     * @return ResponseEntity containing the created RequestDto.
     */
    @PostMapping("/purchase")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Create a vehicle purchase request (Customer access)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Purchase request created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or vehicle not available")
            })
    public ResponseEntity<RequestDto> createPurchaseRequest(@Valid @RequestBody CreatePurchaseRequest createRequest, Authentication authentication) {
        String customerUsername = authentication.getName();
        RequestDto createdRequest = requestService.createPurchaseRequest(createRequest, customerUsername);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    /**
     * Creates a new vehicle service request.
     * Requires CUSTOMER role.
     *
     * @param createRequest DTO for service request.
     * @param authentication Current user's authentication.
     * @return ResponseEntity containing the created RequestDto.
     */
    @PostMapping("/service")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Create a vehicle service request (Customer access)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Service request created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or vehicle not owned by customer")
            })
    public ResponseEntity<RequestDto> createServiceRequest(@Valid @RequestBody CreateServiceRequest createRequest, Authentication authentication) {
        String customerUsername = authentication.getName();
        RequestDto createdRequest = requestService.createServiceRequest(createRequest, customerUsername);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    /**
     * Creates a new vehicle inspection request.
     * Requires CUSTOMER role.
     *
     * @param createRequest DTO for inspection request.
     * @param authentication Current user's authentication.
     * @return ResponseEntity containing the created RequestDto.
     */
    @PostMapping("/inspection")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Create a vehicle inspection request (Customer access)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Inspection request created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<RequestDto> createInspectionRequest(@Valid @RequestBody CreateInspectionRequest createRequest, Authentication authentication) {
        String customerUsername = authentication.getName();
        RequestDto createdRequest = requestService.createInspectionRequest(createRequest, customerUsername);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    /**
     * Retrieves all requests.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @return ResponseEntity containing a list of all RequestDto.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Get all requests (Employee/Manager access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved all requests"))
    public ResponseEntity<List<RequestDto>> getAllRequests() {
        List<RequestDto> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Retrieves requests made by the currently authenticated customer.
     * Requires CUSTOMER role.
     *
     * @param authentication Current user's authentication.
     * @return ResponseEntity containing a list of RequestDto.
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Get requests made by the current customer (Customer access)",
            responses = @ApiResponse(responseCode = "200", description = "Successfully retrieved customer's requests"))
    public ResponseEntity<List<RequestDto>> getMyRequests(Authentication authentication) {
        String customerUsername = authentication.getName();
        List<RequestDto> requests = requestService.getRequestsByCustomerUsername(customerUsername);
        return ResponseEntity.ok(requests);
    }

    /**
     * Retrieves a specific request by its ID.
     * Accessible by EMPLOYEE, MANAGER, or the CUSTOMER who owns the request.
     *
     * @param id The ID of the request.
     * @param authentication Current user's authentication.
     * @return ResponseEntity containing the RequestDto.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER') or @requestSecurityService.isOwnerOfRequest(authentication, #id)")
    @Operation(summary = "Get request by ID (Employee/Manager or Owner access)",
            parameters = @Parameter(name = "id", description = "ID of the request to retrieve", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved request"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to view this request"),
                    @ApiResponse(responseCode = "404", description = "Request not found")
            })
    public ResponseEntity<RequestDto> getRequestById(@PathVariable Long id, Authentication authentication) {
        RequestDto request = requestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }

    /**
     * Accepts a pending request.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param id The ID of the request to accept.
     * @param authentication Current user's authentication.
     * @return ResponseEntity containing the updated RequestDto.
     */
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Accept a pending request (Employee/Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the request to accept", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request accepted successfully"),
                    @ApiResponse(responseCode = "400", description = "Request not in pending state or other processing error"),
                    @ApiResponse(responseCode = "404", description = "Request or Employee not found")
            })
    public ResponseEntity<RequestDto> acceptRequest(@PathVariable Long id, Authentication authentication) {
        String employeeUsername = authentication.getName();
        RequestDto updatedRequest = requestService.acceptRequest(id, employeeUsername);
        return ResponseEntity.ok(updatedRequest);
    }

    /**
     * Rejects a pending request.
     * Requires EMPLOYEE or MANAGER role.
     *
     * @param id The ID of the request to reject.
     * @param authentication Current user's authentication.
     * @return ResponseEntity containing the updated RequestDto.
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')")
    @Operation(summary = "Reject a pending request (Employee/Manager access)",
            parameters = @Parameter(name = "id", description = "ID of the request to reject", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request rejected successfully"),
                    @ApiResponse(responseCode = "400", description = "Request not in pending state or other processing error"),
                    @ApiResponse(responseCode = "404", description = "Request or Employee not found")
            })
    public ResponseEntity<RequestDto> rejectRequest(@PathVariable Long id, Authentication authentication) {
        String employeeUsername = authentication.getName();
        RequestDto updatedRequest = requestService.rejectRequest(id, employeeUsername);
        return ResponseEntity.ok(updatedRequest);
    }
}