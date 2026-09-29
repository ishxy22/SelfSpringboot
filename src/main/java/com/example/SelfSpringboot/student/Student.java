package com.example.SelfSpringboot.student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
// @Table allows specifying schema and unique constraints across multiple columns
@Table(
    name = "student",
    schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(name = "student_email_unique", columnNames = "email")
    }
)
// Lombok Annotations
@Getter
@Setter
@NoArgsConstructor // Required by JPA
@AllArgsConstructor // Generates constructor with ALL fields
@ToString(exclude = "password") // Excludes sensitive data from toString()
public class Student {

    @Id
    @SequenceGenerator(
        name = "student_sequence",
        sequenceName = "student_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "student_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Date of Birth is mandatory")
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Transient // Not stored in DB column, calculated dynamically
    private Integer age;

    @Column(name = "password", nullable = false)
    private String password;

    // Automatic Audit Timestamps
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Custom constructor without id & timestamps for clean initialization
    public Student(String name, String email, LocalDate dob, String password) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.password = password;
    }

    // Dynamic Age Calculation
    public Integer getAge() {
        if (this.dob == null) return null;
        return Period.between(this.dob, LocalDate.now()).getYears();
    }
}