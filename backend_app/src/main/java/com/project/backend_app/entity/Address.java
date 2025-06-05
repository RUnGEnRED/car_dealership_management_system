package com.project.backend_app.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an embeddable address object.
 * This class can be embedded within other entities like Person.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private String city;
    private String postalCode;
    private String country;
}