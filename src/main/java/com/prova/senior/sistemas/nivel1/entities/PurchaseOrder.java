package com.prova.senior.sistemas.nivel1.entities;

import com.prova.senior.sistemas.nivel1.dtos.PurchaseOrderDto;
import com.prova.senior.sistemas.nivel1.enums.OrderStatusEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    @PositiveOrZero(message = "Desconto informado deve ser maior ou igual a 0")
    private BigDecimal discount = new BigDecimal("0");

    @Column
    @PositiveOrZero(message = "Valor total do pedido deve ser maior ou igual a 0")
    private BigDecimal totalValue = new BigDecimal("0");

    @Column
    @NotBlank(message = "Situação do pedido inválida ou não informada")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    public PurchaseOrder convertToEntity(PurchaseOrderDto orderToConvert) {
        PurchaseOrder order = new PurchaseOrder();

        order.setDiscount(orderToConvert.getDiscount().setScale(0, RoundingMode.DOWN));
        order.setItems(orderToConvert.getItems());

        return order;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "id=" + id +
                ", discount=" + discount +
                '}';
    }
}
