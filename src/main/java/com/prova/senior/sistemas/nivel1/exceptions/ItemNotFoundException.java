package com.prova.senior.sistemas.nivel1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
