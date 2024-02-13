package com.prova.senior.sistemas.nivel1.dtos;

import com.prova.senior.sistemas.nivel1.entities.Item;
import com.prova.senior.sistemas.nivel1.entities.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class PurchaseOrderDto {
    private UUID id;

    private BigDecimal discount;

//    private List<ItemDto> itemsDto;

    private List<Item> items;

    private List<Product> products;
}
