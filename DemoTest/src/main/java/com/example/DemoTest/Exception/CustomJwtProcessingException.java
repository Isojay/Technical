package com.example.DemoTest.Exception;


public class CustomJwtProcessingException extends RuntimeException {
    public CustomJwtProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}