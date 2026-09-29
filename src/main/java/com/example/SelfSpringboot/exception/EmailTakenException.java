package com.example.SelfSpringboot.exception;

public class EmailTakenException extends RuntimeException{
    public EmailTakenException(String message) {
        super(message);
    }
}
