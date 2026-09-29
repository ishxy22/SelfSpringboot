package com.example.SelfSpringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleStudentNotFoundException(StudentNotFoundException ex) {
        ErrorMessage error = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ErrorMessage> handleEmailTakenException(EmailTakenException ex) {
        ErrorMessage error = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
    // Extracts the custom message defined in your @NotBlank/@Email annotation
    String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

    ErrorMessage error = new ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            defaultMessage,
            LocalDateTime.now()
    );
    
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
}