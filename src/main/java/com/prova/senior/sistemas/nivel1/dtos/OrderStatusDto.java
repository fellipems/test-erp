package com.prova.senior.sistemas.nivel1.dtos;

import com.prova.senior.sistemas.nivel1.enums.OrderStatusEnum;
import lombok.Data;

@Data
public class OrderStatusDto {
    OrderStatusEnum orderStatus;
}
