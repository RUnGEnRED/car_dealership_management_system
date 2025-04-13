package com.project.backend_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // Optional in recent Spring Boot versions if HttpSecurity bean is present
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/test").permitAll() // Allow access to /api/test without login
                                .requestMatchers("/login").permitAll()   // Allow access to the login page itself
                                .anyRequest().authenticated()           // Require authentication for ALL OTHER requests
                )
                .formLogin(withDefaults()); // Enable default form login (you can customize this later)
        // You might also need to configure CSRF, CORS, OAuth2 etc. depending on your app
        // Example: .csrf(csrf -> csrf.disable()) // Disable CSRF if using stateless API (like JWT)

        return http.build();
    }

    // You might also need a PasswordEncoder Bean if you configure users
    // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    // import org.springframework.security.crypto.password.PasswordEncoder;
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
}