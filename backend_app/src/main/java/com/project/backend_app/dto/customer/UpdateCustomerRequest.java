package com.project.backend_app.dto.customer;

import com.project.backend_app.dto.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating customer information.
 * Fields are optional; only provided fields will be updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 20, message = "Phone number can be at most 20 characters")
    private String phoneNumber;

    @Valid // Ensures nested validation of AddressDto
    private AddressDto address;

    // Username and password changes are typically handled by separate, more secure endpoints.
    // Active status might be changed by an admin through a different mechanism.
    // For simplicity, it's omitted here but can be added if needed.
}