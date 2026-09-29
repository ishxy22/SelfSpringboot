package com.example.SelfSpringboot.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student ishita = new Student(
                "Ishita",
                "ishitayadav2236@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 2),
                
                "pass123"
            );

            Student sahil = new Student(
                "Sahil",
                "sahilchnd03@gmail.com",
                LocalDate.of(2002, Month.JANUARY, 2),
                
                "pass456"
            );

            // Save students to PostgreSQL database
            repository.saveAll(
                List.of(ishita, sahil)
            );
        };
    }
}