package com.project.backend_app.config;

import com.project.backend_app.security.UserDetailsServiceImpl;
import com.project.backend_app.security.jwt.AuthEntryPointJwt;
import com.project.backend_app.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Main Spring Security configuration class.
 * Configures authentication, authorization, JWT handling, and CORS.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Provides a PasswordEncoder bean for hashing passwords.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the DaoAuthenticationProvider.
     *
     * @return A DaoAuthenticationProvider instance.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides the AuthenticationManager bean.
     *
     * @param authConfig The AuthenticationConfiguration.
     * @return An AuthenticationManager instance.
     * @throws Exception If an error occurs while getting the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the main security filter chain.
     * Defines URL authorization rules, session management, and filter order.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Apply CORS configuration
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (authentication, public vehicle listings, Swagger)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/vehicles", "/api/vehicles/available", "/api/vehicles/{id}").permitAll() // Assuming basic vehicle info is public
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // Customer specific endpoints
                        .requestMatchers("/api/customers/me/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/requests/my", "/api/requests/purchase", "/api/requests/service", "/api/requests/inspection").hasRole("CUSTOMER")
                        // Employee/Manager specific endpoints
                        .requestMatchers("/api/requests/{id}/accept", "/api/requests/{id}/reject").hasAnyRole("EMPLOYEE", "MANAGER")
                        .requestMatchers("/api/requests", "/api/requests/{id}").hasAnyRole("EMPLOYEE", "MANAGER", "CUSTOMER") // Customer can see their own, Employee/Manager can see all/specific
                        // Employee/Manager specific "/me" endpoints
                        .requestMatchers(HttpMethod.GET, "/api/employees/me").hasAnyRole("EMPLOYEE", "MANAGER") // GET /me
                        .requestMatchers(HttpMethod.PUT, "/api/employees/me").hasAnyRole("EMPLOYEE", "MANAGER") // PUT /me
                        // Manager specific endpoints for creating/managing employees & customers
                        .requestMatchers("/api/employees", "/api/employees/{id:\\d+}").hasRole("MANAGER") // GET all, GET by ID, PUT by ID, DELETE by ID (for managers)
                        .requestMatchers("/api/auth/register/employee").hasRole("MANAGER")
                        .requestMatchers("/api/customers", "/api/customers/{id}").hasAnyRole("EMPLOYEE","MANAGER")
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    /**
//     * Configures CORS settings.
//     * Allows all origins, methods, and headers for simplicity during development.
//     * Adjust this for production environments.
//     *
//     * @return A CorsFilter instance.
//     */
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:8080", "http://localhost:3001")); // Add your frontend URL and other allowed origins
//        config.addAllowedHeader("*"); // Allow all headers
//        config.addAllowedMethod("*"); // Allow all methods (GET, POST, PUT, DELETE, etc.)
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

    /**
     * Defines the CORS configuration source.
     * This is an alternative way to configure CORS if not using CorsFilter bean directly.
     *
     * @return UrlBasedCorsConfigurationSource for HttpSecurity.cors()
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:8080", "http://localhost:3001")); // Add your frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Allow all HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}