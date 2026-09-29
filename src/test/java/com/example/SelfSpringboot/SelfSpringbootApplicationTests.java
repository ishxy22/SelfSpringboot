package com.example.SelfSpringboot;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.SelfSpringboot.dto.StudentRequestDto;
import com.example.SelfSpringboot.dto.StudentResponseDto;
import com.example.SelfSpringboot.student.Student;
import com.example.SelfSpringboot.student.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SelfSpringbootApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CacheManager cacheManager;

    @SpyBean
    private StudentRepository studentRepositorySpy;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudentAndCacheIt() throws Exception {
        StudentRequestDto requestDto = new StudentRequestDto(
                "Ishita",
                "ishita@example.com",
                LocalDate.of(2000, Month.JANUARY, 1),
                "password123"
        );

        MvcResult result = mockMvc.perform(post("/api/v1/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        StudentResponseDto createdStudent = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                StudentResponseDto.class
        );
        
        // Changed from createdStudent.id() to createdStudent.getId()
        Long studentId = createdStudent.getId();

        Assertions.assertTrue(studentRepository.findById(studentId).isPresent());

        Cache cache = cacheManager.getCache("student");
        assertNotNull(cache);
        assertNotNull(cache.get(studentId, StudentResponseDto.class));
    }

    @Test
    void testGetStudentAndVerifyCache() throws Exception {
        Student student = new Student(
                "Sahil",
                "sahil@example.com",
                LocalDate.of(2001, Month.FEBRUARY, 10),
                "password123"
        );
        student = studentRepository.save(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sahil"));

        Mockito.verify(studentRepositorySpy, Mockito.times(1)).findById(student.getId());

        Mockito.clearInvocations(studentRepositorySpy);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sahil"));

        Mockito.verify(studentRepositorySpy, Mockito.times(0)).findById(student.getId());
    }

    @Test
    void testDeleteStudentAndEvictCache() throws Exception {
        Student student = new Student(
                "Aarav",
                "aarav@example.com",
                LocalDate.of(2002, Month.MARCH, 15),
                "password123"
        );
        student = studentRepository.save(student);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/" + student.getId()))
                .andExpect(status().isOk());

        Assertions.assertFalse(studentRepository.findById(student.getId()).isPresent());

        Cache cache = cacheManager.getCache("student");
        assertNotNull(cache);
        Assertions.assertNull(cache.get(student.getId()));
    }
}