package com.project.backend_app.service.impl;

import com.project.backend_app.dto.customer.CustomerDto;
import com.project.backend_app.dto.customer.UpdateCustomerRequest;
import com.project.backend_app.dto.vehicle.VehicleDto;
import com.project.backend_app.entity.Customer;
import com.project.backend_app.entity.Vehicle;
import com.project.backend_app.repository.CustomerRepository;
import com.project.backend_app.service.CustomerService;
import com.project.backend_app.service.mapper.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the CustomerService interface.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return EntityDtoMapper.toCustomerDto(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found with username: " + username));
        return EntityDtoMapper.toCustomerDto(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(EntityDtoMapper::toCustomerDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerDto updateCustomerProfile(Long customerId, UpdateCustomerRequest updateRequest, String authenticatedUsername) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        // Authorization check: Now only Customer can update their own profile.
        if (!customer.getUsername().equals(authenticatedUsername)) {
            // Might be worth adding permissions for admin/manager.
            boolean isAdminOrManager = false; // Placeholder: Check if authenticatedUser has admin/manager role.
            if (!isAdminOrManager) {
                throw new AccessDeniedException("You are not authorized to update this customer's profile.");
            }
        }

        if (updateRequest.getFirstName() != null) {
            customer.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            customer.setLastName(updateRequest.getLastName());
        }
        // Email update might require re-verification.
        // For simplicity, direct update is allowed here. Check for uniqueness if email is changed.
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(customer.getEmail())) {
            if(customerRepository.existsByEmail(updateRequest.getEmail())) {
                throw new RuntimeException("Error: Email " + updateRequest.getEmail() + " is already in use!");
            }
            customer.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhoneNumber() != null) {
            customer.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getAddress() != null) {
            customer.setAddress(EntityDtoMapper.toAddressEntity(updateRequest.getAddress()));
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return EntityDtoMapper.toCustomerDto(updatedCustomer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customer.setActive(false);
        customerRepository.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customer.setActive(true);
        customerRepository.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> getOwnedVehicles(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        // Ensure Vehicle.owner is fetched eagerly or explicitly if needed.
        // For LAZY fetching, this direct access might cause issues if not within a transaction or if not properly fetched.
        return customer.getOwnedVehicles().stream()
                .filter(Vehicle::isActive)
                .map(EntityDtoMapper::toVehicleDto)
                .collect(Collectors.toList());
    }
}