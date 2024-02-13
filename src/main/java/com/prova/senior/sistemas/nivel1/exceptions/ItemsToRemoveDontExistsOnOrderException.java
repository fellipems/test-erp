package com.prova.senior.sistemas.nivel1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemsToRemoveDontExistsOnOrderException extends RuntimeException {
    public ItemsToRemoveDontExistsOnOrderException() {
    }

    public ItemsToRemoveDontExistsOnOrderException(String message) {
        super(message);
    }
}
