package com.prova.senior.sistemas.nivel1.dtos.filters;

import com.prova.senior.sistemas.nivel1.enums.OrderStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PurchaseOrderFilterDto {
    private UUID id;

    private BigDecimal discount;

    private BigDecimal totalValue;

    private OrderStatusEnum orderStatus;
}
