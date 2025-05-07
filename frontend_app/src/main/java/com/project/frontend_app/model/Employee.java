package com.project.frontend_app.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents an employee in the car dealership management system.
 * Contains personal information and contact details.
 */
public class Employee {
    private static final AtomicLong idCounter = new AtomicLong();

    /** Unique identifier for the employee */
    private Long id;

    /** Employee's first name */
    private String firstName;

    /** Employee's last name */
    private String lastName;

    /** Employee's phone number */
    private String phoneNumber;

    /** Employee's email address */
    private String email;

    /** Employee's role or position in the dealership */
    private String position;

    /**
     * Creates a new Employee instance with the specified details.
     * @param firstName Employee's first name
     * @param lastName Employee's last name
     * @param phoneNumber Employee's phone number
     * @param email Employee's email address
     * @param position Employee's position or role
     */
    public Employee(String firstName, String lastName, String phoneNumber, String email, String position) {
        this.id = idCounter.incrementAndGet();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
    }

    /** Gets the employee's ID */
    public Long getId() { return id; }

    /** Gets the employee's first name */
    public String getFirstName() { return firstName; }

    /** Sets the employee's first name */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** Gets the employee's last name */
    public String getLastName() { return lastName; }

    /** Sets the employee's last name */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** Gets the employee's phone number */
    public String getPhoneNumber() { return phoneNumber; }

    /** Sets the employee's phone number */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /** Gets the employee's email address */
    public String getEmail() { return email; }

    /** Sets the employee's email address */
    public void setEmail(String email) { this.email = email; }

    /** Gets the employee's position */
    public String getPosition() { return position; }

    /** Sets the employee's position */
    public void setPosition(String position) { this.position = position; }

    /**
     * Returns a string representation of the employee
     * @return String in format "FirstName LastName (ID: X, Position: Y)"
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + " (ID: " + id + ", Position: " + position + ")";
    }
}
