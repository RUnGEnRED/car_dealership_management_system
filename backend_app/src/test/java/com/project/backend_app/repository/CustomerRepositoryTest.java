package com.project.backend_app.repository;

import com.project.backend_app.entity.Address;
import com.project.backend_app.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link CustomerRepository}.
 */
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll(); // Ensure a clean state

        customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setUsername("johndoe");
        customer1.setPassword("encodedPassword");
        customer1.setAddress(new Address("123 Main St", "TestCity", "12345", "TestCountry"));
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_CUSTOMER");
        customer1.setRoles(roles);
        customer1.setActive(true);

        customerRepository.save(customer1);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void whenFindByUsername_withExistingUsername_thenReturnCustomer() {
        Optional<Customer> found = customerRepository.findByUsername("johndoe");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void whenFindByUsername_withNonExistingUsername_thenReturnEmpty() {
        Optional<Customer> found = customerRepository.findByUsername("nonexistentuser");
        assertThat(found).isNotPresent();
    }

    @Test
    void whenFindByEmail_withExistingEmail_thenReturnCustomer() {
        Optional<Customer> found = customerRepository.findByEmail("john.doe@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("johndoe");
    }

    @Test
    void whenFindByEmail_withNonExistingEmail_thenReturnEmpty() {
        Optional<Customer> found = customerRepository.findByEmail("no.email@example.com");
        assertThat(found).isNotPresent();
    }

    @Test
    void whenExistsByUsername_withExistingUsername_thenReturnTrue() {
        boolean exists = customerRepository.existsByUsername("johndoe");
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByUsername_withNonExistingUsername_thenReturnFalse() {
        boolean exists = customerRepository.existsByUsername("nonexistentuser");
        assertThat(exists).isFalse();
    }

    @Test
    void whenExistsByEmail_withExistingEmail_thenReturnTrue() {
        boolean exists = customerRepository.existsByEmail("john.doe@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByEmail_withNonExistingEmail_thenReturnFalse() {
        boolean exists = customerRepository.existsByEmail("no.email@example.com");
        assertThat(exists).isFalse();
    }
}