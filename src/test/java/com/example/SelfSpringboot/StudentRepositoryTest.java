package com.example.SelfSpringboot;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// Explicit imports for your Student entity and StudentRepository
import com.example.SelfSpringboot.student.Student;
import com.example.SelfSpringboot.student.StudentRepository;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository repository;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        repository.deleteAll(); // Clean repository before each test
        
        student1 = new Student("Ishita", "ishita@gmail.com", LocalDate.of(2000, Month.JANUARY, 2), "pass123");
        student2 = new Student("Sahil", "sahil@gmail.com", LocalDate.of(2002, Month.JANUARY, 2), "pass123");
    }

    @Test
    void testSave() {
        Student savedStudent = repository.save(student1);
        assertNotNull(savedStudent.getId());
        assertEquals("Ishita", savedStudent.getName());
    }

    @Test
    void testFindById() {
        Student saved = repository.save(student1);
        Optional<Student> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("ishita@gmail.com", found.get().getEmail());
    }

    @Test
    void testSaveAllAndFindAll() {
        repository.saveAll(List.of(student1, student2));
        List<Student> students = repository.findAll();
        assertEquals(2, students.size());
    }

    @Test
    void testUpdateUsingSave() {
        Student saved = repository.save(student1);
        saved.setName("Ishita Updated");
        
        Student updated = repository.save(saved);
        assertEquals("Ishita Updated", updated.getName());
    }

    @Test
    void testCount() {
        repository.saveAll(List.of(student1, student2));
        long count = repository.count();
        assertEquals(2, count);
    }

    @Test
    void testDeleteById() {
        Student saved = repository.save(student1);
        repository.deleteById(saved.getId());
        
        Optional<Student> result = repository.findById(saved.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteEntity() {
        Student saved = repository.save(student1);
        repository.delete(saved);
        
        boolean exists = repository.existsById(saved.getId());
        assertFalse(exists);
    }

    @Test
    void testDeleteAll() {
        repository.saveAll(List.of(student1, student2));
        repository.deleteAll();
        
        assertEquals(0, repository.count());
    }
}