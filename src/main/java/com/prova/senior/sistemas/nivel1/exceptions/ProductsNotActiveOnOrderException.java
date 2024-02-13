package com.prova.senior.sistemas.nivel1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductsNotActiveOnOrderException extends RuntimeException {

    public ProductsNotActiveOnOrderException() {
    }

    public ProductsNotActiveOnOrderException(String message) {
        super(message);
    }
}
