package com.example.SelfSpringboot.student;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SelfSpringboot.dto.StudentRequestDto;
import com.example.SelfSpringboot.dto.StudentResponseDto;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getStudents() {
        List<StudentResponseDto> students = studentService.getStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // 2. REGISTER NEW STUDENT - Returns 201 CREATED
    @PostMapping
    public ResponseEntity<String> registerNewStudent(@Valid @RequestBody StudentRequestDto requestDto) {
        studentService.addNewStudent(requestDto);
        return new ResponseEntity<>("Student registered successfully", HttpStatus.CREATED);
    }

    // 3. DELETE STUDENT - Returns 200 OK or 204 NO CONTENT
    @DeleteMapping(path = "{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok("Student with ID " + studentId + " deleted successfully");
    }

    // 4. UPDATE STUDENT - Returns 200 OK
    @PutMapping(path = "{studentId}")
    public ResponseEntity<String> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        studentService.updateStudent(studentId, name, email);
        return ResponseEntity.ok("Student details updated successfully");
    }
}

  

