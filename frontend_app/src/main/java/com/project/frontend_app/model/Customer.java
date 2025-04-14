package com.project.frontend_app.model;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Represents a customer in the car dealership management system.
 * Contains personal information and address details.
 */
public class Customer {
    private static final AtomicLong idCounter = new AtomicLong();
    
    /** Unique identifier for the customer */
    private Long id;
    
    /** Customer's first name */
    private String firstName;
    
    /** Customer's last name */
    private String lastName;
    
    /** Customer's phone number */
    private String phoneNumber;
    
    /** Customer's email address */
    private String email;
    
    /** Customer's physical address */
    private Address address;

    /**
     * Creates a new Customer instance with the specified details.
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param phoneNumber Customer's phone number
     * @param email Customer's email address
     * @param address Customer's physical address
     */
    public Customer(String firstName, String lastName, String phoneNumber, String email, Address address) {
        this.id = idCounter.incrementAndGet();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    /**
     * Gets the customer's ID
     * @return The customer's unique identifier
     */
    public Long getId() { return id; }

    /**
     * Gets the customer's first name
     * @return The first name
     */
    public String getFirstName() { return firstName; }

    /**
     * Sets the customer's first name
     * @param firstName The new first name
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Gets the customer's last name
     * @return The last name
     */
    public String getLastName() { return lastName; }

    /**
     * Sets the customer's last name
     * @param lastName The new last name
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Gets the customer's phone number
     * @return The phone number
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Sets the customer's phone number
     * @param phoneNumber The new phone number
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Gets the customer's email address
     * @return The email address
     */
    public String getEmail() { return email; }

    /**
     * Sets the customer's email address
     * @param email The new email address
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Gets the customer's address
     * @return The address object
     */
    public Address getAddress() { return address; }

    /**
     * Sets the customer's address
     * @param address The new address
     */
    public void setAddress(Address address) { this.address = address; }

    /**
     * Gets the customer's full address as a formatted string
     * @return Formatted address string or empty string if address is null
     */
    public String getFullAddress() {
        return address != null ? address.toString() : "";
    }

    /**
     * Returns a string representation of the customer
     * @return String in format "FirstName LastName (ID: X)"
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + " (ID: " + id + ")";
    }
}