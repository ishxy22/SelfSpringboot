package com.example.SelfSpringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabled for testing REST APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/student/**").authenticated() // Secure student API
                .anyRequest().permitAll()                              // Allow public access to all other endpoints
            )
            .formLogin(Customizer.withDefaults()); // Explicitly enables Form Login mechanism

        return http.build();
    }
}