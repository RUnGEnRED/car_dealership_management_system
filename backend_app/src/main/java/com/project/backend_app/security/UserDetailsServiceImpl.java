package com.project.backend_app.security;

import com.project.backend_app.entity.Customer;
import com.project.backend_app.entity.Employee;
import com.project.backend_app.entity.Person; // Common superclass
import com.project.backend_app.repository.CustomerRepository;
import com.project.backend_app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implements Spring Security's UserDetailsService.
 * Loads user-specific data by username from Customer or Employee repositories.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Loads the user by their username.
     * It first tries to find a customer, then an employee.
     *
     * @param username The username identifying the user whose data is required.
     * @return A UserDetails object containing user information.
     * @throws UsernameNotFoundException If the user could not be found.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customerOpt = customerRepository.findByUsername(username);
        if (customerOpt.isPresent()) {
            return buildUserDetails(customerOpt.get());
        }

        Optional<Employee> employeeOpt = employeeRepository.findByUsername(username);
        if (employeeOpt.isPresent()) {
            return buildUserDetails(employeeOpt.get());
        }

        throw new UsernameNotFoundException("User Not Found with username: " + username);
    }

    /**
     * Helper method to build UserDetails from a Person entity.
     *
     * @param person The Person entity (Customer or Employee).
     * @return UserDetails object.
     */
    private UserDetails buildUserDetails(Person person) {
        Collection<? extends GrantedAuthority> authorities = person.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                person.getUsername(),
                person.getPassword(),
                person.isActive(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}