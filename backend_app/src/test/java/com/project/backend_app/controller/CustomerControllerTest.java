package com.project.backend_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.customer.UpdateCustomerRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


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
 * Integration tests for {@link CustomerController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private CustomerDto customerDto1;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        addressDto = new AddressDto("Street 1", "City 1", "P1C1", "Country 1");
        customerDto1 = new CustomerDto(1L, "Customer", "One", "cust1@example.com", "111222333",
                addressDto, "custuser1", true);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void whenGetAllCustomers_asEmployee_thenReturnCustomerList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(customerDto1));
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("custuser1")));
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"}) // Customer cannot get all customers
    void whenGetAllCustomers_asCustomer_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "custuser1", roles = {"CUSTOMER"})
    void whenGetCurrentCustomerProfile_asOwner_thenReturnCustomerDto() throws Exception {
        when(customerService.getCustomerByUsername("custuser1")).thenReturn(customerDto1);
        mockMvc.perform(get("/api/customers/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("custuser1")));
    }

    @Test
    @WithMockUser(username = "custuser1", roles = {"CUSTOMER"})
    void whenUpdateCurrentCustomerProfile_asOwner_withValidData_thenReturnOk() throws Exception {
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest("UpdatedFName", null, null, null, null);
        CustomerDto updatedDto = new CustomerDto(1L, "UpdatedFName", "One", "cust1@example.com", "111222333",
                addressDto, "custuser1", true);

        // Mock getCustomerByUsername to resolve ID, then mock updateCustomerProfile
        when(customerService.getCustomerByUsername("custuser1")).thenReturn(customerDto1);
        when(customerService.updateCustomerProfile(eq(1L), any(UpdateCustomerRequest.class), eq("custuser1"))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/customers/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("UpdatedFName")));
    }

    @Test
    @WithMockUser(username = "custuser1", roles = {"CUSTOMER"})
    void whenGetMyOwnedVehicles_asOwner_thenReturnVehicles() throws Exception {
        // Assume VehicleDto structure
        List<VehicleDto> ownedVehicles = List.of(new VehicleDto(10L, "VINOWNED1", "MyCarMake", "MyCarModel", 2020, null, null, null, 1L, true));
        when(customerService.getCustomerByUsername("custuser1")).thenReturn(customerDto1);
        when(customerService.getOwnedVehicles(1L)).thenReturn(ownedVehicles);

        mockMvc.perform(get("/api/customers/me/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vin", is("VINOWNED1")));
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    void whenDeactivateCustomer_asManager_thenReturnNoContent() throws Exception {
        doNothing().when(customerService).deactivateCustomer(1L);
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    // Might add tests for other endpoints and roles (e.g., /api/customers/{id}, /api/customers/{id}/vehicles by manager)
}