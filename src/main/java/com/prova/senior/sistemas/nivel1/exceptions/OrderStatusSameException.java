package com.prova.senior.sistemas.nivel1.exceptions;

import com.prova.senior.sistemas.nivel1.enums.OrderStatusEnum;

public class OrderStatusSameException extends RuntimeException {

    public OrderStatusSameException() {
    }

    public OrderStatusSameException(String message) {
        super(message);
    }
}
