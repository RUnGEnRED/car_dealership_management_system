package com.project.backend_app.service.impl;

import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.dto.employee.UpdateEmployeeRequest;
import com.project.backend_app.entity.Address;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.enums.Position;
import com.project.backend_app.repository.EmployeeRepository;
import com.project.backend_app.service.mapper.EntityDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EmployeeServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee1;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        Address address = new Address("Work St", "OfficeCity", "W0001", "WorkCountry");
        addressDto = EntityDtoMapper.toAddressDto(address);

        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("Emp");
        employee1.setLastName("Loyee");
        employee1.setEmail("emp.loyee@example.com");
        employee1.setUsername("employeelogin");
        employee1.setAddress(address);
        employee1.setPosition(Position.EMPLOYEE);
        employee1.setActive(true);
        employee1.setRoles(Set.of(AuthServiceImpl.ROLE_EMPLOYEE));
    }

    @Test
    void whenGetEmployeeById_withExistingId_thenReturnEmployeeDto() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        EmployeeDto result = employeeService.getEmployeeById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void whenUpdateEmployee_changePositionToManager_thenRolesUpdated() {
        UpdateEmployeeRequest updateRequest = new UpdateEmployeeRequest();
        updateRequest.setPosition(Position.MANAGER);
        // Assume original position was EMPLOYEE
        employee1.setPosition(Position.EMPLOYEE);
        employee1.setRoles(Set.of(AuthServiceImpl.ROLE_EMPLOYEE));


        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeDto result = employeeService.updateEmployee(1L, updateRequest);

        assertThat(result.getPosition()).isEqualTo(Position.MANAGER);
        // Check that the entity passed to save had the correct roles
        verify(employeeRepository).save(argThat(savedEmp ->
                savedEmp.getRoles().contains(AuthServiceImpl.ROLE_EMPLOYEE) &&
                        savedEmp.getRoles().contains(AuthServiceImpl.ROLE_MANAGER)
        ));
    }

    @Test
    void whenUpdateMyProfile_byEmployee_thenPositionNotChanged() {
        UpdateEmployeeRequest updateRequest = new UpdateEmployeeRequest();
        updateRequest.setFirstName("UpdatedEmpName");
        updateRequest.setPosition(Position.MANAGER); // Attempt to change position

        employee1.setPosition(Position.EMPLOYEE); // Original position

        when(employeeRepository.findByUsername("employeelogin")).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeDto result = employeeService.updateMyProfile("employeelogin", updateRequest);

        assertThat(result.getFirstName()).isEqualTo("UpdatedEmpName");
        assertThat(result.getPosition()).isEqualTo(Position.EMPLOYEE); // Position should NOT change
        // Verify the entity passed to save still has original position
        verify(employeeRepository).save(argThat(savedEmp ->
                savedEmp.getPosition() == Position.EMPLOYEE
        ));
    }

    @Test
    void whenDeactivateEmployee_thenSetInactiveAndSave() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(employee1)).thenReturn(employee1);
        employeeService.deactivateEmployee(1L);
        assertThat(employee1.isActive()).isFalse();
        verify(employeeRepository).save(employee1);
    }

    // Might add more tests for other methods: getEmployeeByUsername, getAllEmployees, activateEmployee, etc.
    // similar to CustomerServiceImplTest and VehicleServiceImplTest
}