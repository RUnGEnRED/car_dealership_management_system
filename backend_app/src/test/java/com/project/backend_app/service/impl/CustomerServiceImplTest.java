package com.project.backend_app.service.impl;

import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.customer.UpdateCustomerRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.entity.Address;
import com.project.backend_app.entity.Customer;
import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.repository.CustomerRepository;
import com.project.backend_app.service.mapper.EntityDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CustomerServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer1;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        Address address = new Address("1 Main St", "CityA", "10000", "CountryA");
        addressDto = EntityDtoMapper.toAddressDto(address);

        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("Test");
        customer1.setLastName("User");
        customer1.setEmail("test.user@example.com");
        customer1.setUsername("testuser");
        customer1.setAddress(address);
        customer1.setActive(true);
        customer1.setOwnedVehicles(new ArrayList<>()); // Initialize owned vehicles
    }

    @Test
    void whenGetCustomerById_withExistingId_thenReturnCustomerDto() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        CustomerDto result = customerService.getCustomerById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(customerRepository).findById(1L);
    }

    @Test
    void whenGetCustomerById_withNonExistingId_thenThrowRuntimeException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> customerService.getCustomerById(99L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void whenGetCustomerByUsername_withExistingUsername_thenReturnCustomerDto() {
        when(customerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer1));
        CustomerDto result = customerService.getCustomerByUsername("testuser");
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void whenGetAllCustomers_thenReturnListOfCustomerDtos() {
        when(customerRepository.findAll()).thenReturn(List.of(customer1));
        List<CustomerDto> results = customerService.getAllCustomers();
        assertThat(results).isNotNull().hasSize(1);
    }

    @Test
    void whenUpdateCustomerProfile_byOwner_thenSuccess() {
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest("Updated", "User", null, null, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer1); // Simulate save returns updated

        CustomerDto result = customerService.updateCustomerProfile(1L, updateRequest, "testuser");

        assertThat(result.getFirstName()).isEqualTo("Updated");
        verify(customerRepository).save(customer1);
    }

    @Test
    void whenUpdateCustomerProfile_byOwnerWithEmailChange_andEmailAvailable_thenSuccess() {
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest(null, null, "new.email@example.com", null, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(customerRepository.existsByEmail("new.email@example.com")).thenReturn(false); // New email is available
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        CustomerDto result = customerService.updateCustomerProfile(1L, updateRequest, "testuser");

        assertThat(result.getEmail()).isEqualTo("new.email@example.com");
        assertThat(customer1.getEmail()).isEqualTo("new.email@example.com");
        verify(customerRepository).save(customer1);
    }

    @Test
    void whenUpdateCustomerProfile_byOwnerWithEmailChange_andEmailTaken_thenThrowException() {
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest(null, null, "taken.email@example.com", null, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(customerRepository.existsByEmail("taken.email@example.com")).thenReturn(true); // New email is taken

        assertThatThrownBy(() -> customerService.updateCustomerProfile(1L, updateRequest, "testuser"))
                .isInstanceOf(RuntimeException.class) // Or DuplicateResourceException
                .hasMessageContaining("Email taken.email@example.com is already in use!");

        verify(customerRepository, never()).save(any(Customer.class));
    }


    @Test
    void whenUpdateCustomerProfile_byNonOwner_andNotAdmin_thenThrowAccessDenied() {
        // Note: The current service impl has a placeholder for admin check.
        // This test assumes the basic check where non-owner non-admin is denied.
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest("Malicious", "Update", null, null, null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        assertThatThrownBy(() -> customerService.updateCustomerProfile(1L, updateRequest, "anotheruser"))
                .isInstanceOf(AccessDeniedException.class);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void whenDeactivateCustomer_thenSetInactiveAndSave() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(customerRepository.save(customer1)).thenReturn(customer1);
        customerService.deactivateCustomer(1L);
        assertThat(customer1.isActive()).isFalse();
        verify(customerRepository).save(customer1);
    }

    @Test
    void whenActivateCustomer_thenSetToActiveAndSave() {
        customer1.setActive(false); // Start with inactive
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(customerRepository.save(customer1)).thenReturn(customer1);
        customerService.activateCustomer(1L);
        assertThat(customer1.isActive()).isTrue();
        verify(customerRepository).save(customer1);
    }

    @Test
    void whenGetOwnedVehicles_withExistingCustomer_thenReturnVehicleDtos() {
        Vehicle ownedVehicle = new Vehicle();
        ownedVehicle.setId(10L);
        ownedVehicle.setVin("OWNEDVIN");
        ownedVehicle.setActive(true); // Important: only active vehicles
        customer1.setOwnedVehicles(List.of(ownedVehicle));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        List<VehicleDto> results = customerService.getOwnedVehicles(1L);
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getVin()).isEqualTo("OWNEDVIN");
    }

    @Test
    void whenGetOwnedVehicles_withInactiveOwnedVehicle_thenNotReturnInactiveVehicle() {
        Vehicle activeOwnedVehicle = new Vehicle();
        activeOwnedVehicle.setId(10L);
        activeOwnedVehicle.setVin("ACTIVEOWNEDVIN");
        activeOwnedVehicle.setActive(true);

        Vehicle inactiveOwnedVehicle = new Vehicle();
        inactiveOwnedVehicle.setId(11L);
        inactiveOwnedVehicle.setVin("INACTIVEOWNEDVIN");
        inactiveOwnedVehicle.setActive(false);

        customer1.setOwnedVehicles(List.of(activeOwnedVehicle, inactiveOwnedVehicle));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        List<VehicleDto> results = customerService.getOwnedVehicles(1L);
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getVin()).isEqualTo("ACTIVEOWNEDVIN");
    }
}