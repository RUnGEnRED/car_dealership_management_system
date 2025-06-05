package com.project.backend_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend_app.dto.request.CreatePurchaseRequest;
import com.project.backend_app.dto.request.RequestDto;
import com.project.backend_app.entity.enums.RequestStatus;
import com.project.backend_app.entity.enums.RequestType;
import com.project.backend_app.security.RequestSecurityService; // For mocking isOwnerOfRequest
import com.project.backend_app.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link RequestController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private RequestService requestService;
    @MockBean private RequestSecurityService requestSecurityService;

    private RequestDto purchaseRequestDto;

    @BeforeEach
    void setUp() {
        purchaseRequestDto = new RequestDto(1L, 10L, "customerUser", 20L, "VEHICLEVIN01",
                null, null, RequestType.PURCHASE, RequestStatus.PENDING, "I want this car",
                LocalDateTime.now(), null);
    }

    @Test
    @WithMockUser(username = "customerUser", roles = "CUSTOMER")
    void whenCreatePurchaseRequest_asCustomer_withValidData_thenReturnCreated() throws Exception {
        CreatePurchaseRequest createDto = new CreatePurchaseRequest(20L, "I want this car");
        when(requestService.createPurchaseRequest(any(CreatePurchaseRequest.class), eq("customerUser")))
                .thenReturn(purchaseRequestDto);

        mockMvc.perform(post("/api/requests/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestType", is(RequestType.PURCHASE.toString())))
                .andExpect(jsonPath("$.customerUsername", is("customerUser")));
    }

    @Test
    @WithMockUser(username = "customerUser", roles = "CUSTOMER")
    void whenGetMyRequests_asCustomer_thenReturnRequests() throws Exception {
        when(requestService.getRequestsByCustomerUsername("customerUser")).thenReturn(List.of(purchaseRequestDto));
        mockMvc.perform(get("/api/requests/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @WithMockUser(username = "employeeUser", roles = "EMPLOYEE")
    void whenAcceptRequest_asEmployee_thenReturnOk() throws Exception {
        RequestDto acceptedRequestDto = new RequestDto(1L, 10L, "customerUser", 20L, "VEHICLEVIN01",
                30L, "employeeUser", RequestType.PURCHASE, RequestStatus.ACCEPTED, "I want this car",
                LocalDateTime.now(), LocalDateTime.now());
        when(requestService.acceptRequest(eq(1L), eq("employeeUser"))).thenReturn(acceptedRequestDto);

        mockMvc.perform(put("/api/requests/1/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(RequestStatus.ACCEPTED.toString())))
                .andExpect(jsonPath("$.assignedEmployeeUsername", is("employeeUser")));
    }

    @Test
    @WithMockUser(username = "customerUser", roles = "CUSTOMER") // Customer is owner
    void whenGetRequestById_asOwner_thenReturnRequest() throws Exception {
        when(requestSecurityService.isOwnerOfRequest(any(Authentication.class), eq(1L))).thenReturn(true);
        when(requestService.getRequestById(1L)).thenReturn(purchaseRequestDto);

        mockMvc.perform(get("/api/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(username = "otherCustomer", roles = "CUSTOMER") // Customer is NOT owner
    void whenGetRequestById_asNonOwnerCustomer_thenReturnForbidden() throws Exception {
        when(requestSecurityService.isOwnerOfRequest(any(Authentication.class), eq(1L))).thenReturn(false);

        mockMvc.perform(get("/api/requests/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "managerUser", roles = "MANAGER") // Manager can access any request
    void whenGetRequestById_asManager_thenReturnRequest() throws Exception {
        when(requestSecurityService.isOwnerOfRequest(any(Authentication.class), eq(1L))).thenReturn(false);
        when(requestService.getRequestById(1L)).thenReturn(purchaseRequestDto);

        mockMvc.perform(get("/api/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }


    // Might add tests for:
    // - createServiceRequest, createInspectionRequest
    // - getAllRequests (as employee/manager)
    // - rejectRequest
    // - Scenarios where dependent resources (customer, vehicle, employee) are not found for request operations.
    // - Validation failures for create DTOs.
}