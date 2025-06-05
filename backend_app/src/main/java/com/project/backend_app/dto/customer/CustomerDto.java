package com.project.backend_app.dto.customer;

import com.project.backend_app.dto.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing customer data in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDto address;
    private String username;
    private boolean active;

    // ownedVehicles list might be better handled by a separate endpoint or a slimmer DTO
    // For simplicity, it's omitted here but can be added if needed.
}