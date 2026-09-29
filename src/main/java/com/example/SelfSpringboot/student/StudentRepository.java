package com.example.SelfSpringboot.student;

import java.util.Optional;

// Use fully-qualified name for JpaRepository to avoid unresolved import issues
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends org.springframework.data.jpa.repository.JpaRepository<Student, Long> {

// SELECT * FROM student WHERE email=?
 //   @Query("SELECT s FROM Student s WHERE s.email=?1")
    Optional<Student> findStudentByEmail(String email);
}