package com.project.backend_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.dto.employee.UpdateEmployeeRequest;
import com.project.backend_app.entity.enums.Position;
import com.project.backend_app.service.EmployeeService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link EmployeeController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private EmployeeService employeeService;

    private EmployeeDto managerDto;
    private EmployeeDto employeeDto;
    private AddressDto addressDto;


    @BeforeEach
    void setUp() {
        addressDto = new AddressDto("Emp St", "Emp City", "E1P E2S", "Emp Country");
        managerDto = new EmployeeDto(1L, "Manager", "User", "manager@example.com", "123",
                addressDto, "manageruser", Position.MANAGER, true);
        employeeDto = new EmployeeDto(2L, "Employee", "Worker", "employee@example.com", "456",
                addressDto, "employeeuser", Position.EMPLOYEE, true);
    }

    @Test
    @WithMockUser(username="admin", roles = "MANAGER")
    void whenGetAllEmployees_asManager_thenReturnEmployeeList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of(managerDto, employeeDto));
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username="employeeuser", roles = "EMPLOYEE")
    void whenGetAllEmployees_asEmployee_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="employeeuser", roles = "EMPLOYEE")
    void whenGetCurrentEmployeeProfile_asEmployee_thenReturnProfile() throws Exception {
        when(employeeService.getEmployeeByUsername("employeeuser")).thenReturn(employeeDto);
        mockMvc.perform(get("/api/employees/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("employeeuser")));
    }

    @Test
    @WithMockUser(username="employeeuser", roles = {"EMPLOYEE"})
    void whenUpdateMyEmployeeProfile_asEmployee_withValidData_thenReturnOk() throws Exception {
        UpdateEmployeeRequest updateRequest = new UpdateEmployeeRequest();
        updateRequest.setFirstName("UpdatedEmpName");

        EmployeeDto updatedDto = new EmployeeDto(2L, "UpdatedEmpName", "Worker", "employee@example.com", "456",
                addressDto, "employeeuser", Position.EMPLOYEE, true);

        when(employeeService.updateMyProfile(eq("employeeuser"), any(UpdateEmployeeRequest.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/employees/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("UpdatedEmpName")))
                .andExpect(jsonPath("$.position", is(Position.EMPLOYEE.toString())));
    }


    // Might add more tests:
    // - Get employee by ID (as manager)
    // - Update employee (as manager, including position change)
    // - Deactivate employee (as manager)
}