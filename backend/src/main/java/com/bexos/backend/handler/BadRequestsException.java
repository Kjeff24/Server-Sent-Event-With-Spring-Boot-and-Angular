package com.bexos.backend.handler;

public class BadRequestsException extends RuntimeException {
    public BadRequestsException(String message) {
        super(message);
    }
}
