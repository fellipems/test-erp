package com.prova.senior.sistemas.nivel1.services;

import com.prova.senior.sistemas.nivel1.dtos.ProductDTO;
import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.exceptions.ProductHaveRelationshipException;
import com.prova.senior.sistemas.nivel1.exceptions.ProductNotFoundException;
import com.prova.senior.sistemas.nivel1.repositories.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(ProductDTO newProduct) {
        Product product = new Product().convertToEntity(newProduct);

        return repository.save(product);
    }

    public Product findOneById(UUID productId) {
        return repository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Nenhum produto encontrado com ID " + productId));
    }

    public List<Product> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public String deleteById(UUID productId) {
        Product productToDelete = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Nenhum produto encontrado com ID " + productId));

        try {
            repository.delete(productToDelete);
        } catch (DataIntegrityViolationException exception) {
            throw new ProductHaveRelationshipException("Não é possível remover um produto que já esteja vinculado a um pedido");
        }

        return "Produto removido com sucesso";
    }

    public Product update(UUID productId, ProductDTO updatedProductDto) {
        Product oldProduct = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Nenhum produto encontrado com ID " + productId));

        if (updatedProductDto.getName() != null) {
            oldProduct.setName(updatedProductDto.getName());
        }

        if (updatedProductDto.getPrice() != null) {
            oldProduct.setPrice(updatedProductDto.getPrice().setScale(2, RoundingMode.DOWN));
        }

        return repository.save(oldProduct);
    }

    public Page<Product> findAllPageable(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return repository.findAll(pageable);
    }
}
