package com.flowired.exception;

public class ModelValidationException extends RuntimeException {
    public ModelValidationException(String message) {
        super(message);
    }
}
