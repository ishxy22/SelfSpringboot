package com.example.SelfSpringboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class StudentRequestDto {

    @NotBlank(message = "Name is mandatory and cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Date of Birth is mandatory")
    private LocalDate dob;

    private String password;

    // Default Constructor
    public StudentRequestDto() {}

    // Parameterized Constructor
    public StudentRequestDto(String name, String email, LocalDate dob, String password) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.password=password;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}