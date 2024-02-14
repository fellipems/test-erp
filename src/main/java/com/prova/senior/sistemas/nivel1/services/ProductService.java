package com.prova.senior.sistemas.nivel1.services;

import com.prova.senior.sistemas.nivel1.dtos.ProductDTO;
import com.prova.senior.sistemas.nivel1.dtos.filters.ProductFilterDto;
import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.entities.QProduct;
import com.prova.senior.sistemas.nivel1.exceptions.ProductHaveRelationshipException;
import com.prova.senior.sistemas.nivel1.exceptions.ProductNotFoundException;
import com.prova.senior.sistemas.nivel1.repositories.ProductDslRepository;
import com.prova.senior.sistemas.nivel1.repositories.ProductRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;

    private final ProductDslRepository productDslRepository;

    public ProductService(ProductRepository repository, ProductDslRepository productDslRepository) {
        this.repository = repository;
        this.productDslRepository = productDslRepository;
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

        if (updatedProductDto.getType() != null) {
            oldProduct.setType(updatedProductDto.getType());
        }

        if (updatedProductDto.isActive() != oldProduct.isActive()) {
            oldProduct.setActive(updatedProductDto.isActive());
        }

        return repository.save(oldProduct);
    }

    public Page<Product> findAllPageable(int page, int size, String sortBy, String direction, ProductFilterDto productFilter) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (StringUtils.hasText(productFilter.getName())) {
            predicateBuilder.and(QProduct.product.name.like(productFilter.getName()));
        }

        if (Objects.nonNull(productFilter.getType()) && StringUtils.hasText(productFilter.getType().name())) {
            predicateBuilder.and(QProduct.product.type.eq(productFilter.getType()));
        }

        if (Objects.nonNull(productFilter.getId())) {
            predicateBuilder.and(QProduct.product.id.eq(productFilter.getId()));
        }

        if (Objects.nonNull(productFilter.getPrice())) {
            predicateBuilder.and(QProduct.product.price.eq(productFilter.getPrice().setScale(2, RoundingMode.DOWN)));
        }

        if (Objects.nonNull(productFilter.getActive())) {
            predicateBuilder.and(QProduct.product.active.eq(productFilter.getActive()));
        }

        return productDslRepository.findAll(predicateBuilder, pageable);
    }
}
