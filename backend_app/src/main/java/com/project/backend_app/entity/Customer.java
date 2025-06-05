package com.project.backend_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the car showroom system.
 * Inherits common fields from the Person class.
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // NOT USED IN PROJECT ANYMORE, Important for entities inheriting from MappedSuperclass with Id
public class Customer extends Person {

    /**
     * List of vehicles owned by this customer.
     * A customer can own multiple vehicles.
     */
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Vehicle> ownedVehicles = new ArrayList<>();
}