package com.prova.senior.sistemas.nivel1.entities;

import com.prova.senior.sistemas.nivel1.dtos.ProductDTO;
import com.prova.senior.sistemas.nivel1.enums.Type;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    public Product convertToEntity(ProductDTO productToConvert) {
        Product product = new Product();

        product.setName(productToConvert.getName());
        product.setPrice(productToConvert.getPrice().setScale(2, RoundingMode.DOWN));
        product.setType(productToConvert.getType());

        return product;
    }
}
