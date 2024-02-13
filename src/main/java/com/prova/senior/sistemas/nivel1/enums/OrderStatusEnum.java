package com.prova.senior.sistemas.nivel1.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum OrderStatusEnum {
    OPEN,
    CLOSED;

    @JsonCreator
    public static OrderStatusEnum fromString(String value) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(String.format("Status inválido: %s. Status válidos: %s", value, Arrays.toString(OrderStatusEnum.values())));
    }
}
