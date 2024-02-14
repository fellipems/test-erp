package com.prova.senior.sistemas.nivel1.dtos.filters;

import com.prova.senior.sistemas.nivel1.enums.Type;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductFilterDto {
    private UUID id;

    private String name;

    private BigDecimal price;

    private Type type;

    private Boolean active;
}
