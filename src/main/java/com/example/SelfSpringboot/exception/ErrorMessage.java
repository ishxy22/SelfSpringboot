package com.example.SelfSpringboot.exception;

import java.time.LocalDateTime;

public class ErrorMessage {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;

    // No-args constructor
    public ErrorMessage() {
    }

    // All-args constructor
    public ErrorMessage(int statusCode, String message, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;

    }
}
