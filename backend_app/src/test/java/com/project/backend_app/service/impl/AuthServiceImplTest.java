package com.project.backend_app.service.impl;

import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.dto.auth.CustomerRegisterRequest;
import com.project.backend_app.dto.auth.EmployeeRegisterRequest;
import com.project.backend_app.dto.auth.JwtResponse;
import com.project.backend_app.dto.auth.LoginRequest;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.employee.EmployeeDto;
import com.project.backend_app.entity.Customer;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.enums.Position;
import com.project.backend_app.repository.CustomerRepository;
import com.project.backend_app.repository.EmployeeRepository;
import com.project.backend_app.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private CustomerRegisterRequest customerRegisterRequest;
    private EmployeeRegisterRequest employeeRegisterRequest;
    private LoginRequest loginRequest;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        addressDto = new AddressDto("123 Main St", "TestCity", "12345", "TestCountry");

        customerRegisterRequest = new CustomerRegisterRequest(
                "John", "Doe", "john.doe@example.com", "1234567890",
                addressDto, "johndoe", "password123"
        );

        employeeRegisterRequest = new EmployeeRegisterRequest(
                "Jane", "Smith", "jane.smith@example.com", "0987654321",
                addressDto, "janesmith", "password123", Position.EMPLOYEE
        );

        loginRequest = new LoginRequest("testuser", "password");
    }

    // --- registerCustomer Tests ---
    @Test
    void whenRegisterCustomer_withValidData_thenReturnCustomerDto() {
        // Given
        when(customerRepository.existsByUsername(customerRegisterRequest.getUsername())).thenReturn(false);
        when(customerRepository.existsByEmail(customerRegisterRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(customerRegisterRequest.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(1L); // Simulate save
            return c;
        });

        // When
        CustomerDto result = authService.registerCustomer(customerRegisterRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(customerRegisterRequest.getUsername());
        assertThat(result.getEmail()).isEqualTo(customerRegisterRequest.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void whenRegisterCustomer_withExistingUsername_thenThrowRuntimeException() {
        // Given
        when(customerRepository.existsByUsername(customerRegisterRequest.getUsername())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.registerCustomer(customerRegisterRequest))
                .isInstanceOf(RuntimeException.class) // Consider DuplicateResourceException
                .hasMessageContaining("Username is already taken!");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void whenRegisterCustomer_withExistingEmail_thenThrowRuntimeException() {
        // Given
        when(customerRepository.existsByUsername(customerRegisterRequest.getUsername())).thenReturn(false);
        when(customerRepository.existsByEmail(customerRegisterRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.registerCustomer(customerRegisterRequest))
                .isInstanceOf(RuntimeException.class) // Consider DuplicateResourceException
                .hasMessageContaining("Email is already in use!");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    // --- registerEmployee Tests ---
    @Test
    void whenRegisterEmployee_asEmployee_thenReturnEmployeeDtoWithCorrectRoles() {
        // Given
        when(employeeRepository.existsByUsername(employeeRegisterRequest.getUsername())).thenReturn(false);
        when(employeeRepository.existsByEmail(employeeRegisterRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(employeeRegisterRequest.getPassword())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(1L);
            // Simulate role setting in service
            Set<String> roles = new HashSet<>();
            roles.add(AuthServiceImpl.ROLE_EMPLOYEE);
            if (e.getPosition() == Position.MANAGER) {
                roles.add(AuthServiceImpl.ROLE_MANAGER);
            }
            e.setRoles(roles);
            return e;
        });

        // When
        EmployeeDto result = authService.registerEmployee(employeeRegisterRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(employeeRegisterRequest.getUsername());
        assertThat(result.getPosition()).isEqualTo(Position.EMPLOYEE);
        // Verify roles are set based on position in actual service method
        verify(employeeRepository).save(argThat(employee ->
                employee.getRoles().contains(AuthServiceImpl.ROLE_EMPLOYEE) &&
                        !employee.getRoles().contains(AuthServiceImpl.ROLE_MANAGER)
        ));
    }

    @Test
    void whenRegisterEmployee_asManager_thenReturnEmployeeDtoWithManagerRole() {
        // Given
        employeeRegisterRequest.setPosition(Position.MANAGER);
        when(employeeRepository.existsByUsername(employeeRegisterRequest.getUsername())).thenReturn(false);
        when(employeeRepository.existsByEmail(employeeRegisterRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(employeeRegisterRequest.getPassword())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(1L);
            Set<String> roles = new HashSet<>();
            roles.add(AuthServiceImpl.ROLE_EMPLOYEE);
            if (e.getPosition() == Position.MANAGER) {
                roles.add(AuthServiceImpl.ROLE_MANAGER);
            }
            e.setRoles(roles);
            return e;
        });

        // When
        EmployeeDto result = authService.registerEmployee(employeeRegisterRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPosition()).isEqualTo(Position.MANAGER);
        verify(employeeRepository).save(argThat(employee ->
                employee.getRoles().contains(AuthServiceImpl.ROLE_EMPLOYEE) &&
                        employee.getRoles().contains(AuthServiceImpl.ROLE_MANAGER)
        ));
    }


    // --- authenticateUser Tests ---
    @Test
    void whenAuthenticateUser_withValidCredentialsForCustomer_thenReturnJwtResponse() {
        // Given
        Authentication authentication = mock(Authentication.class);
        User userDetails = new User(loginRequest.getUsername(), "encodedPassword",
                Collections.singletonList(new SimpleGrantedAuthority(AuthServiceImpl.ROLE_CUSTOMER)));
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUsername(loginRequest.getUsername());
        customer.setEmail("customer@example.com");


        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("mocked.jwt.token");
        when(customerRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(customer));

        // When
        JwtResponse result = authService.authenticateUser(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("mocked.jwt.token");
        assertThat(result.getUsername()).isEqualTo(loginRequest.getUsername());
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("customer@example.com");
        assertThat(result.getRoles()).contains(AuthServiceImpl.ROLE_CUSTOMER);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    void whenAuthenticateUser_withValidCredentialsForEmployee_thenReturnJwtResponse() {
        // Given
        Authentication authentication = mock(Authentication.class);
        User userDetails = new User(loginRequest.getUsername(), "encodedPassword",
                List.of(new SimpleGrantedAuthority(AuthServiceImpl.ROLE_EMPLOYEE), new SimpleGrantedAuthority(AuthServiceImpl.ROLE_MANAGER)));
        Employee employee = new Employee();
        employee.setId(2L);
        employee.setUsername(loginRequest.getUsername());
        employee.setEmail("manager@example.com");

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("mocked.jwt.token.employee");
        when(customerRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());
        when(employeeRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(employee));

        // When
        JwtResponse result = authService.authenticateUser(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("mocked.jwt.token.employee");
        assertThat(result.getUsername()).isEqualTo(loginRequest.getUsername());
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getEmail()).isEqualTo("manager@example.com");
        assertThat(result.getRoles()).containsExactlyInAnyOrder(AuthServiceImpl.ROLE_EMPLOYEE, AuthServiceImpl.ROLE_MANAGER);
    }
}