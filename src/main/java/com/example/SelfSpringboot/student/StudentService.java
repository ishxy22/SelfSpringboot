package com.example.SelfSpringboot.student;

import com.example.SelfSpringboot.dto.StudentRequestDto;
import com.example.SelfSpringboot.dto.StudentResponseDto;
import com.example.SelfSpringboot.exception.EmailTakenException;
import com.example.SelfSpringboot.exception.StudentNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Helper method: Entity -> Response DTO
    private StudentResponseDto mapToResponseDto(Student student) {
        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getDob(),
                student.getAge()
        );
    }

    // 1. GET ALL STUDENTS (Caches the entire list)
    @Transactional(readOnly = true)
    @Cacheable(value = "students", key = "'all'")
    public List<StudentResponseDto> getStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // 2. GET SINGLE STUDENT BY ID (Caches specific student)
    @Transactional(readOnly = true)
    @Cacheable(value = "student", key = "#studentId")
    public StudentResponseDto getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student with ID " + studentId + " does not exist"
                ));
        return mapToResponseDto(student);
    }

    // 3. ADD STUDENT (Saves to DB and automatically caches using the returned object's ID)
    @Transactional
    @CachePut(value = "student", key = "#result.id")
    @CacheEvict(value = "students", key = "'all'") // Evicts "all" cache list so new student appears
    public StudentResponseDto addNewStudent(StudentRequestDto requestDto) {
        if (studentRepository.findStudentByEmail(requestDto.getEmail()).isPresent()) {
            throw new EmailTakenException("Email " + requestDto.getEmail() + " is already taken");
        }

        Student student = new Student(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getDob(),
                requestDto.getPassword()
        );

        Student savedStudent = studentRepository.save(student);
        return mapToResponseDto(savedStudent);
    }

    // 4. DELETE STUDENT (Evicts the student from Redis by ID)
    @Transactional
    @CacheEvict(value = {"student", "students"}, key = "#studentId")
    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);

        if (!exists) {
            throw new StudentNotFoundException("Student with ID " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    // 5. UPDATE STUDENT (Evicts outdated cached student and full list)
    @Transactional
    @CacheEvict(value = {"student", "students"}, allEntries = true)
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student with ID " + studentId + " does not exist"
                ));

        if (name != null && !name.trim().isEmpty() && !student.getName().equals(name)) {
            student.setName(name);
        }

        if (email != null && !email.trim().isEmpty() && !student.getEmail().equals(email)) {
            if (studentRepository.findStudentByEmail(email).isPresent()) {
                throw new EmailTakenException("Email " + email + " is already taken");
            }
            student.setEmail(email);
        }
    }
}