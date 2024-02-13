package com.prova.senior.sistemas.nivel1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderStatusNotInformedException extends RuntimeException {

    public OrderStatusNotInformedException() {
    }

    public OrderStatusNotInformedException(String message) {
        super(message);
    }
}
