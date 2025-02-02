package com.sistemaligafutbol.sistemaligafutbol.exceptions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageValidationException extends RuntimeException {
    public ImageValidationException(String message) {
        super(message);
    }
}