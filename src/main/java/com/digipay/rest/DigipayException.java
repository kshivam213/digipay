package com.digipay.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DigipayException extends RuntimeException{

    private final HttpStatus status;
    private final String message;

    public DigipayException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
