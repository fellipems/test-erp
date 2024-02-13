package com.prova.senior.sistemas.nivel1.dtos;

import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.entities.PurchaseOrder;
import lombok.Data;

import java.util.UUID;

@Data
public class ItemDto {
    private UUID id;

    private int quantity;

    private Product product;

    private PurchaseOrder purchaseOrder;

    private UUID productId;
}
