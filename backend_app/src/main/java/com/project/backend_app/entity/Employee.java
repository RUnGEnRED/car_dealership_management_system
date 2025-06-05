package com.project.backend_app.entity;

import com.project.backend_app.entity.enums.Position;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents an employee in the car showroom system.
 * Inherits common fields from the Person class.
 */
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employee extends Person {

    /**
     * The position held by the employee (e.g., MANAGER, EMPLOYEE).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;
}