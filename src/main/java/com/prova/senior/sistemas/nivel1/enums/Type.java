package com.prova.senior.sistemas.nivel1.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Type {
    PRODUCT,
    SERVICE;

    @JsonCreator
    public static Type fromString(String value) {
        for (Type type : Type.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException(String.format("Tipo inválido: %s. Tipos válidos: %s", value, Arrays.toString(Type.values())));
    }
}
