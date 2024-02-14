package com.prova.senior.sistemas.nivel1.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prova.senior.sistemas.nivel1.dtos.ItemDto;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    @Positive(message = "Quantidade informada deve ser maior que zero")
    private int quantity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @NotNull(message = "Produto não informado no item")
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id")
    @JsonIgnoreProperties("items")
    private PurchaseOrder purchaseOrder;

    public Item convertToEntity(ItemDto itemToConvert) {
        Item item = new Item();

        item.setQuantity(itemToConvert.getQuantity());
        item.setProduct(itemToConvert.getProduct());
        item.setPurchaseOrder(itemToConvert.getPurchaseOrder());

        return item;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
