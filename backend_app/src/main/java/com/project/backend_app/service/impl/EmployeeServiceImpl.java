package com.project.backend_app.service.impl;

import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.dto.employee.UpdateEmployeeRequest;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.enums.Position;
import com.project.backend_app.repository.EmployeeRepository;
import com.project.backend_app.service.EmployeeService;
import com.project.backend_app.service.mapper.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the EmployeeService interface.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    // Role constants from AuthServiceImpl or a shared constants class
    private final String ROLE_EMPLOYEE = AuthServiceImpl.ROLE_EMPLOYEE;
    private final String ROLE_MANAGER = AuthServiceImpl.ROLE_MANAGER;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return EntityDtoMapper.toEmployeeDto(employee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found with username: " + username));
        return EntityDtoMapper.toEmployeeDto(employee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EntityDtoMapper::toEmployeeDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EmployeeDto updateEmployee(Long id, UpdateEmployeeRequest updateRequest) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        if (updateRequest.getFirstName() != null) {
            employee.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            employee.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(employee.getEmail())) {
            if(employeeRepository.existsByEmail(updateRequest.getEmail())) {
                throw new RuntimeException("Error: Email " + updateRequest.getEmail() + " is already in use!");
            }
            employee.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhoneNumber() != null) {
            employee.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getAddress() != null) {
            employee.setAddress(EntityDtoMapper.toAddressEntity(updateRequest.getAddress()));
        }
        if (updateRequest.getPosition() != null && updateRequest.getPosition() != employee.getPosition()) {
            employee.setPosition(updateRequest.getPosition());
            // Update roles accordingly
            Set<String> roles = new HashSet<>();
            roles.add(ROLE_EMPLOYEE); // All employees are at least ROLE_EMPLOYEE
            if (employee.getPosition() == Position.MANAGER) {
                roles.add(ROLE_MANAGER);
            }
            employee.setRoles(roles);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return EntityDtoMapper.toEmployeeDto(updatedEmployee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void activateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employee.setActive(true);
        employeeRepository.save(employee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EmployeeDto updateMyProfile(String authenticatedUsername, UpdateEmployeeRequest updateRequest) {
        Employee employee = employeeRepository.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new RuntimeException("Employee not found with username: " + authenticatedUsername));

        // Employees cannot change their own position via this method.
        // That is handled by a manager through the updateEmployee(Long id, ...) method.
        if (updateRequest.getPosition() != null) {
            // Log this attempt or simply ignore it. For simplicity, we ignore.
            // throw new AccessDeniedException("Employees cannot change their own position.");
        }

        if (updateRequest.getFirstName() != null) {
            employee.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            employee.setLastName(updateRequest.getLastName());
        }
        // Email update might require re-verification.
        // For simplicity, direct update is allowed here. Check for uniqueness if email is changed.
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(employee.getEmail())) {
            if(employeeRepository.existsByEmail(updateRequest.getEmail())) {
                throw new RuntimeException("Error: Email " + updateRequest.getEmail() + " is already in use!");
            }
            employee.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhoneNumber() != null) {
            employee.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getAddress() != null) {
            employee.setAddress(EntityDtoMapper.toAddressEntity(updateRequest.getAddress()));
        }
        // Note: Active status change is typically an admin/manager action.

        Employee updatedEmployee = employeeRepository.save(employee);
        return EntityDtoMapper.toEmployeeDto(updatedEmployee);
    }
}