package com.project.backend_app.dto.employee;

import com.project.backend_app.dto.AddressDto;
import com.project.backend_app.entity.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing employee data in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDto address;
    private String username;
    private Position position;
    private boolean active;
}