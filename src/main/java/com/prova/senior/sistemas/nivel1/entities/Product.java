package com.prova.senior.sistemas.nivel1.entities;

import com.prova.senior.sistemas.nivel1.dtos.ProductDTO;
import com.prova.senior.sistemas.nivel1.enums.Type;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
    @NotEmpty(message = "Nome não informado")
    @Size(min = 3, max = 256, message = "Nome deve conter entre 3 e 256 caracteres")
    private String name;

    @Column
    @NotNull(message = "Preço do produto não informado")
    @PositiveOrZero(message = "Número informado deve ser maior ou igual a zero")
    private BigDecimal price;

    @Column
    @NotNull(message = "Tipo do produto não informado")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    public Product() {}

    public Product(String name, BigDecimal price, Type type, boolean active) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.active = active;
    }

    public Product convertToEntity(ProductDTO productToConvert) {
        Product product = new Product();

        product.setName(productToConvert.getName());
        product.setPrice(productToConvert.getPrice() != null ? productToConvert.getPrice().setScale(2, RoundingMode.DOWN) : new BigDecimal("0"));
        product.setType(productToConvert.getType());

        return product;
    }
}
