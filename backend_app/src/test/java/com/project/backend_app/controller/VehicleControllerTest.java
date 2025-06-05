package com.project.backend_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend_app.dto.vehicle.CreateVehicleRequest;
import com.project.backend_app.dto.vehicle.UpdateVehicleRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.entity.enums.VehicleAvailability;
import com.project.backend_app.entity.enums.VehicleCondition;
import com.project.backend_app.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean; // For mocking services
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // For testing secured endpoints
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link VehicleController}.
 * Uses @SpringBootTest to load the full application context.
 * Uses @AutoConfigureMockMvc to enable and configure MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    private VehicleDto vehicleDto1;
    private VehicleDto vehicleDto2;

    @BeforeEach
    void setUp() {
        vehicleDto1 = new VehicleDto(1L, "VINPUBLIC01", "PublicMake1", "PublicModel1", 2022,
                new BigDecimal("30000.00"), VehicleCondition.NEW, VehicleAvailability.AVAILABLE, null, true);
        vehicleDto2 = new VehicleDto(2L, "VINPUBLIC02", "PublicMake2", "PublicModel2", 2023,
                new BigDecimal("35000.00"), VehicleCondition.USED, VehicleAvailability.RESERVED, null, true);
    }

    // --- Public Endpoints ---

    @Test
    void whenGetAllActiveVehicles_publicAccess_thenReturnListOfVehicles() throws Exception {
        // Given
        when(vehicleService.getAllActiveVehicles()).thenReturn(List.of(vehicleDto1, vehicleDto2));

        // When & Then
        mockMvc.perform(get("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vin", is("VINPUBLIC01")))
                .andExpect(jsonPath("$[1].vin", is("VINPUBLIC02")));
    }

    @Test
    void whenGetAllActiveAvailableVehicles_publicAccess_thenReturnListOfAvailableVehicles() throws Exception {
        // Given
        when(vehicleService.getAllActiveAvailableVehicles()).thenReturn(List.of(vehicleDto1)); // Only vehicleDto1 is AVAILABLE

        // When & Then
        mockMvc.perform(get("/api/vehicles/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vin", is("VINPUBLIC01")))
                .andExpect(jsonPath("$[0].availability", is(VehicleAvailability.AVAILABLE.toString())));
    }

    @Test
    void whenGetVehicleById_publicAccess_withExistingActiveVehicle_thenReturnVehicle() throws Exception {
        // Given
        when(vehicleService.getVehicleById(1L)).thenReturn(vehicleDto1);

        // When & Then
        mockMvc.perform(get("/api/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.vin", is("VINPUBLIC01")));
    }

    // --- Secured Endpoints ---

    @Test
    @WithMockUser(roles = "EMPLOYEE") // Simulates an authenticated user with ROLE_EMPLOYEE
    void whenCreateVehicle_asEmployee_withValidData_thenReturnCreated() throws Exception {
        // Given
        CreateVehicleRequest createRequest = new CreateVehicleRequest(
                "VALIDVIN123456789", "SecuredMake", "SecuredModel", 2024,
                new BigDecimal("40000.00"), VehicleCondition.NEW
        );
        VehicleDto createdVehicleDto = new VehicleDto(3L, "NEWVINSECURED", "SecuredMake", "SecuredModel", 2024,
                new BigDecimal("40000.00"), VehicleCondition.NEW, VehicleAvailability.AVAILABLE, null, true);

        when(vehicleService.createVehicle(any(CreateVehicleRequest.class))).thenReturn(createdVehicleDto);

        // When & Then
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.vin", is("NEWVINSECURED")));
    }

    @Test
    @WithMockUser // Simulates a basic authenticated user without specific roles
    void whenCreateVehicle_asUnathorizedUser_thenReturnForbidden() throws Exception {
        // Given
        CreateVehicleRequest createRequest = new CreateVehicleRequest(
                "VALIDVIN123456789", "ForbiddenMake", "ForbiddenModel", 2024,
                new BigDecimal("40000.00"), VehicleCondition.NEW
        );
        // No need to mock service as it shouldn't be reached

        // When & Then
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenCreateVehicle_asUnauthenticatedUser_thenReturnUnauthorized() throws Exception {
        // Given
        CreateVehicleRequest createRequest = new CreateVehicleRequest(
                "VALIDVIN123456789", "UnauthMake", "UnauthModel", 2024,
                new BigDecimal("40000.00"), VehicleCondition.NEW
        );

        // When & Then
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    void whenUpdateVehicle_asManager_withValidData_thenReturnOk() throws Exception {
        // Given
        UpdateVehicleRequest updateRequest = new UpdateVehicleRequest();
        updateRequest.setMake("UpdatedByManager");
        updateRequest.setPrice(new BigDecimal("32000.00"));

        VehicleDto updatedVehicleDto = new VehicleDto(1L, "VINPUBLIC01", "UpdatedByManager", "PublicModel1", 2022,
                new BigDecimal("32000.00"), VehicleCondition.NEW, VehicleAvailability.AVAILABLE, null, true);

        when(vehicleService.updateVehicle(eq(1L), any(UpdateVehicleRequest.class))).thenReturn(updatedVehicleDto);

        // When & Then
        mockMvc.perform(put("/api/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make", is("UpdatedByManager")))
                .andExpect(jsonPath("$.price", is(32000.00)));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void whenDeleteVehicle_asManager_thenReturnNoContent() throws Exception {
        // Given
        doNothing().when(vehicleService).deactivateVehicle(1L);

        // When & Then
        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE") // Employee cannot delete
    void whenDeleteVehicle_asEmployee_thenReturnForbidden() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isForbidden());
    }

    // Might add more tests for:
    // - Validation errors (e.g., creating a vehicle with invalid VIN)
    // - Resource not found scenarios for PUT/DELETE/GET by ID
    // - Different user roles for various secured endpoints
}