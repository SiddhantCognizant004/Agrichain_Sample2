package com.cts.Registration_Service.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public UserNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}