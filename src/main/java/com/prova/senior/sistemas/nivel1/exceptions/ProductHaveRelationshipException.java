package com.prova.senior.sistemas.nivel1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductHaveRelationshipException extends RuntimeException {
    public ProductHaveRelationshipException() {
    }

    public ProductHaveRelationshipException(String message) {
        super(message);
    }
}
