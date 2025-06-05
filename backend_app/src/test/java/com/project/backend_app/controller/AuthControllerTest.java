package com.project.backend_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.dto.auth.CustomerRegisterRequest;
import com.project.backend_app.dto.auth.EmployeeRegisterRequest;
import com.project.backend_app.dto.auth.JwtResponse;
import com.project.backend_app.dto.auth.LoginRequest;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.entity.enums.Position;
import com.project.backend_app.service.AuthService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link AuthController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private CustomerRegisterRequest customerRegisterRequest;
    private EmployeeRegisterRequest employeeRegisterRequest;
    private LoginRequest loginRequest;
    private AddressDto addressDto;


    @BeforeEach
    void setUp() {
        addressDto = new AddressDto("123 Test St", "Testville", "T1S T2S", "Testland");
        customerRegisterRequest = new CustomerRegisterRequest("Test", "Cust", "testcust@example.com",
                "1231231234", addressDto, "testcustuser", "password123");

        employeeRegisterRequest = new EmployeeRegisterRequest("Test", "Emp", "testemp@example.com",
                "3213214321", addressDto, "testempuser", "password456", Position.EMPLOYEE);

        loginRequest = new LoginRequest("testuser", "password");
    }

    @Test
    void whenRegisterCustomer_withValidData_thenReturnCreated() throws Exception {
        CustomerDto customerDto = new CustomerDto(1L, "Test", "Cust", "testcust@example.com",
                "1231231234", addressDto, "testcustuser", true);
        when(authService.registerCustomer(any(CustomerRegisterRequest.class))).thenReturn(customerDto);

        mockMvc.perform(post("/api/auth/register/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRegisterRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("testcustuser")));
    }

    @Test
    @WithMockUser(roles = "MANAGER") // Needed to pass security for this endpoint
    void whenRegisterEmployee_asManager_withValidData_thenReturnCreated() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(1L, "Test", "Emp", "testemp@example.com",
                "3213214321", addressDto, "testempuser", Position.EMPLOYEE, true);
        when(authService.registerEmployee(any(EmployeeRegisterRequest.class))).thenReturn(employeeDto);

        mockMvc.perform(post("/api/auth/register/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRegisterRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("testempuser")));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE") // Employee cannot register another employee
    void whenRegisterEmployee_asEmployee_thenReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/auth/register/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRegisterRequest)))
                .andExpect(status().isForbidden());
    }


    @Test
    void whenSignin_withValidCredentials_thenReturnJwtResponse() throws Exception {
        JwtResponse jwtResponse = new JwtResponse("test.jwt.token", 1L, "testuser", "testuser@example.com", List.of("ROLE_CUSTOMER"));
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("test.jwt.token")))
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    // Might add tests for invalid registration (e.g., missing fields to trigger 400 from validation)
    // Might add tests for failed signin (to trigger 401, if authService is mocked to throw AuthenticationException)
}