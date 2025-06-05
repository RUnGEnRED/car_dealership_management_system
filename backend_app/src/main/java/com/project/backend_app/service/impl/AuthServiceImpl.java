package com.project.backend_app.service.impl;

import com.project.backend_app.dto.auth.CustomerRegisterRequest;
import com.project.backend_app.dto.auth.EmployeeRegisterRequest;
import com.project.backend_app.dto.auth.JwtResponse;
import com.project.backend_app.dto.auth.LoginRequest;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.entity.Customer;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.enums.Position; // Assuming ROLE_ enums are defined or handled by constants
import com.project.backend_app.repository.CustomerRepository;
import com.project.backend_app.repository.EmployeeRepository;
import com.project.backend_app.service.AuthService;
import com.project.backend_app.service.mapper.EntityDtoMapper; // Your mapper class
import com.project.backend_app.security.jwt.JwtTokenProvider; // Your JWT provider
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Implementation of the AuthService interface.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // Define role constants - these should align with your Spring Security configuration
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";


    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           CustomerRepository customerRepository,
                           EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerDto registerCustomer(CustomerRegisterRequest registerRequest) {
        if (customerRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (customerRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        Customer customer = EntityDtoMapper.toCustomerEntity(registerRequest);
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Set<String> roles = new HashSet<>();
        roles.add(ROLE_CUSTOMER);
        customer.setRoles(roles);

        Customer savedCustomer = customerRepository.save(customer);
        return EntityDtoMapper.toCustomerDto(savedCustomer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EmployeeDto registerEmployee(EmployeeRegisterRequest registerRequest) {
        if (employeeRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (employeeRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        Employee employee = EntityDtoMapper.toEmployeeEntity(registerRequest);
        employee.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(ROLE_EMPLOYEE); // All employees are at least ROLE_EMPLOYEE
        if (registerRequest.getPosition() == Position.MANAGER) {
            roles.add(ROLE_MANAGER);
        }
        employee.setRoles(roles);

        Employee savedEmployee = employeeRepository.save(employee);
        return EntityDtoMapper.toEmployeeDto(savedEmployee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        // UserDetails implementation stores the original entity ID or can retrieve it
        // Might need adjustment based on UserDetailsServiceImpl
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId;
        String email;

        // A bit of a workaround to get ID and email, ideally UserDetails carries this
        // Or you make another DB call. For simplicity, this tries to find the user.
        // In a real app, your custom UserDetails object would carry the Person's ID and email.
        var customerOpt = customerRepository.findByUsername(userDetails.getUsername());
        if (customerOpt.isPresent()) {
            userId = customerOpt.get().getId();
            email = customerOpt.get().getEmail();
        } else {
            var employeeOpt = employeeRepository.findByUsername(userDetails.getUsername());
            if (employeeOpt.isPresent()) {
                userId = employeeOpt.get().getId();
                email = employeeOpt.get().getEmail();
            } else {
                throw new RuntimeException("User details not found after authentication"); // Should not happen
            }
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userId, userDetails.getUsername(), email, roles);
    }
}