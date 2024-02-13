package com.prova.senior.sistemas.nivel1.dtos;

import com.prova.senior.sistemas.nivel1.enums.Type;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductDTO {
    private UUID id;

    private String name;

    private BigDecimal price;

    private Type type;
}
