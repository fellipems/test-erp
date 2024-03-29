package com.prova.senior.sistemas.nivel1.controllers;

import com.prova.senior.sistemas.nivel1.dtos.ProductDTO;
import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> allProducts = service.findAll();
        return ResponseEntity.ok().body(allProducts);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDTO newProduct) {
        Product product = service.create(newProduct);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findOneById(@PathVariable UUID productId) {
        Product product = service.findOneById(productId);

        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteById(@PathVariable UUID productId) {
        return ResponseEntity.ok(service.deleteById(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> update(@PathVariable UUID productId, @RequestBody ProductDTO updatedProductDto) {
        return ResponseEntity.ok(service.update(productId, updatedProductDto));
    }
}
