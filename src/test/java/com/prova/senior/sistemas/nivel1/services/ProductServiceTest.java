package com.prova.senior.sistemas.nivel1.services;

import com.prova.senior.sistemas.nivel1.dtos.ProductDTO;
import com.prova.senior.sistemas.nivel1.dtos.filters.ProductFilterDto;
import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.enums.Type;
import com.prova.senior.sistemas.nivel1.exceptions.ProductHaveRelationshipException;
import com.prova.senior.sistemas.nivel1.exceptions.ProductNotFoundException;
import com.prova.senior.sistemas.nivel1.repositories.ProductDslRepository;
import com.prova.senior.sistemas.nivel1.repositories.ProductRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductDslRepository productDslRepository;

    private static UUID productId;

    private static Product expectedProduct;

    private static ProductDTO productDTO;

    @BeforeAll
    public static void setUp() {
        productId = UUID.randomUUID();

        expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setName("Test Product");
        expectedProduct.setPrice(BigDecimal.valueOf(10.50));
        expectedProduct.setType(Type.PRODUCT);
        expectedProduct.setActive(true);

        productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(new BigDecimal("10.50"));
        productDTO.setType(Type.PRODUCT);
    }

    @Test
    public void shouldCreateANewProductIfAllFieldsAreCorrectlySetted() {

        Product expectedProductSaved = expectedProduct;

        when(repository.save(any(Product.class))).thenReturn(expectedProductSaved);

        Product actualProduct = productService.create(productDTO);

        verify(repository, times(1)).save(any(Product.class));

        assertEquals(expectedProductSaved, actualProduct);
    }

    @Test
    public void shouldFindProductById() {
        when(repository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.findOneById(productId);

        verify(repository, times(1)).findById(productId);

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void shouldTrowProductNotFoundExceptionWhenDontFindAnyProductWithInformedId() throws ProductNotFoundException {
        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findOneById(productId));
    }

    @Test
    public void shouldFindAllTwoProducts() {
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product("Product 1", new BigDecimal("10.00"), Type.PRODUCT, true));
        expectedProducts.add(new Product("Product 2", new BigDecimal("20.00"), Type.PRODUCT, true));

        when(repository.findAll(any(Sort.class))).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.findAll();

        verify(repository, times(1)).findAll(any(Sort.class));

        assertEquals(expectedProducts, actualProducts);
        assertEquals(expectedProducts.size(), 2);
    }

    @Test
    void shouldDeleteProductByValidId() {
        when(repository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        String result = productService.deleteById(productId);

        verify(repository, times(1)).findById(productId);

        verify(repository, times(1)).delete(expectedProduct);

        assertEquals("Produto removido com sucesso", result);
    }

    @Test
    void shouldThowExceptionWhenProductsHaveOrders() {
        Product productToDelete = expectedProduct;

        when(repository.findById(productId)).thenReturn(Optional.of(productToDelete));

        doThrow(DataIntegrityViolationException.class).when(repository).delete(productToDelete);

        assertThrows(ProductHaveRelationshipException.class, () -> productService.deleteById(productId));
    }

    @Test
    public void shouldUpdateProduct() {
        ProductDTO updatedProductDto = new ProductDTO();
        updatedProductDto.setName("Updated Product");
        updatedProductDto.setPrice(new BigDecimal("15.00"));
        updatedProductDto.setType(Type.PRODUCT);
        updatedProductDto.setActive(false);

        Product oldProduct = new Product();
        oldProduct.setId(productId);
        oldProduct.setName("Old Product");
        oldProduct.setPrice(new BigDecimal("10.00"));
        oldProduct.setType(Type.PRODUCT);
        oldProduct.setActive(true);

        when(repository.findById(productId)).thenReturn(Optional.of(oldProduct));

        when(repository.save(any(Product.class))).thenReturn(oldProduct);

        Product updatedProduct = productService.update(productId, updatedProductDto);

        verify(repository, times(1)).save(oldProduct);

        assertEquals(updatedProductDto.getName(), updatedProduct.getName());
        assertEquals(updatedProductDto.getPrice(), updatedProduct.getPrice());
        assertEquals(updatedProductDto.getType(), updatedProduct.getType());
        assertEquals(updatedProductDto.isActive(), updatedProduct.isActive());
    }

    @Test
    public void shouldThrowProductNotFoundExceptionWhenNotFoundProductToUpdate() {
        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.update(productId, new ProductDTO()));
    }
}