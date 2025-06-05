package com.project.backend_app.repository;

import com.project.backend_app.entity.Address;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.enums.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link EmployeeRepository}.
 */
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1, manager1;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll(); // Ensure a clean state

        employee1 = new Employee();
        employee1.setFirstName("Jane");
        employee1.setLastName("Doe");
        employee1.setEmail("jane.doe@example.com");
        employee1.setUsername("janedoe");
        employee1.setPassword("encodedPassword");
        employee1.setAddress(new Address("456 Oak St", "WorkCity", "67890", "WorkCountry"));
        employee1.setPosition(Position.EMPLOYEE);
        Set<String> empRoles = new HashSet<>();
        empRoles.add("ROLE_EMPLOYEE");
        employee1.setRoles(empRoles);
        employee1.setActive(true);

        manager1 = new Employee();
        manager1.setFirstName("Peter");
        manager1.setLastName("Manager");
        manager1.setEmail("peter.manager@example.com");
        manager1.setUsername("petermanager");
        manager1.setPassword("encodedPasswordMgr");
        manager1.setAddress(new Address("789 Pine St", "HeadOffice", "54321", "WorkCountry"));
        manager1.setPosition(Position.MANAGER);
        Set<String> mgrRoles = new HashSet<>();
        mgrRoles.add("ROLE_EMPLOYEE");
        mgrRoles.add("ROLE_MANAGER");
        manager1.setRoles(mgrRoles);
        manager1.setActive(true);

        employeeRepository.saveAll(List.of(employee1, manager1));
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void whenFindByUsername_withExistingUsername_thenReturnEmployee() {
        Optional<Employee> found = employeeRepository.findByUsername("janedoe");
        assertThat(found).isPresent();
        assertThat(found.get().getPosition()).isEqualTo(Position.EMPLOYEE);
    }

    @Test
    void whenFindByPosition_withManagerPosition_thenReturnOnlyManagers() {
        List<Employee> managers = employeeRepository.findByPosition(Position.MANAGER);
        assertThat(managers).hasSize(1);
        assertThat(managers.get(0).getUsername()).isEqualTo("petermanager");

        List<Employee> employees = employeeRepository.findByPosition(Position.EMPLOYEE);
        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getUsername()).isEqualTo("janedoe");
    }

    // Might add other tests similar to CustomerRepositoryTest (findByEmail, existsByUsername, existsByEmail)
}