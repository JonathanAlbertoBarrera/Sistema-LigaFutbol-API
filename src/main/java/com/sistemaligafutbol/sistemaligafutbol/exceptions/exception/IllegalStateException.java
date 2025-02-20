package com.sistemaligafutbol.sistemaligafutbol.exceptions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IllegalStateException extends RuntimeException{
    public IllegalStateException(String message) {
        super(message);
    }
}
