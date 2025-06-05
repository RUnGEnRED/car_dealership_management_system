package com.project.backend_app.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for JWT authentication responses.
 */
@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    /**
     * Constructs a new JwtResponse.
     *
     * @param accessToken The JWT access token.
     * @param id The ID of the authenticated user.
     * @param username The username of the authenticated user.
     * @param email The email of the authenticated user.
     * @param roles The roles assigned to the authenticated user.
     */
    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}