package com.project.backend_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * A base class for person entities (Customer, Employee).
 * This class itself is not an entity but its mapping information is applied
 * to the entities that inherit from it.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    @Embedded
    private Address address;

    /**
     * The username used for login. Must be unique and not null.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * The password for the user. Stored as a hash. Must not be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Set of roles assigned to the person, used for authorization.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    /**
     * Indicates if the person's account is active.
     * Used for soft delete functionality. Defaults to true.
     */
    private boolean active = true;
}