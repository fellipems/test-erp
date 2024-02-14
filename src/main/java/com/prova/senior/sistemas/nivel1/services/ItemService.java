package com.prova.senior.sistemas.nivel1.services;

import com.prova.senior.sistemas.nivel1.dtos.ItemDto;
import com.prova.senior.sistemas.nivel1.dtos.filters.ItemFilterDto;
import com.prova.senior.sistemas.nivel1.entities.Item;
import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.entities.QItem;
import com.prova.senior.sistemas.nivel1.entities.QProduct;
import com.prova.senior.sistemas.nivel1.exceptions.ItemNotFoundException;
import com.prova.senior.sistemas.nivel1.exceptions.ProductNotFoundException;
import com.prova.senior.sistemas.nivel1.repositories.ItemDslRepository;
import com.prova.senior.sistemas.nivel1.repositories.ItemRepository;
import com.prova.senior.sistemas.nivel1.repositories.ProductRepository;
import com.prova.senior.sistemas.nivel1.repositories.PurchaseOrderRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {
    private final ItemRepository repository;

    private final ProductRepository productRepository;

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final ItemDslRepository itemDslRepository;

    public ItemService(ItemRepository repository, ProductRepository productRepository, PurchaseOrderRepository purchaseOrderRepository, ItemDslRepository itemDslRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.itemDslRepository = itemDslRepository;
    }

    public Item create(ItemDto newItem) {
        if (newItem.getProduct() != null && newItem.getProduct().getId() != null) {
            Optional<Product> product = productRepository.findById(newItem.getProduct().getId());

            product.ifPresent(newItem::setProduct);
        }

        Item item = new Item().convertToEntity(newItem);

        return repository.save(item);
    }

    public Item findOneById(UUID itemId) {
        return repository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Nenhum item encontrado com ID " + itemId));
    }

    public List<Item> findAll() {
        return repository.findAll();
    }

    public String deleteById(UUID itemId) {
        Item itemToDelete = repository.findById(itemId).orElseThrow(() -> new ProductNotFoundException("Nenhum item encontrado com ID " + itemId));

        repository.delete(itemToDelete);

        return "Item removido com sucesso";
    }

    public Item update(UUID itemId, ItemDto updatedItemDto) {
        Item oldItem = repository.findById(itemId).orElseThrow(() -> new ProductNotFoundException("Nenhum item encontrado com ID " + itemId));

        if (updatedItemDto.getProduct() != null && updatedItemDto.getProduct().getId() != null) {
            productRepository.findById(updatedItemDto.getProduct().getId())
                    .ifPresent(oldItem::setProduct);
        }

        if (updatedItemDto.getQuantity() != 0) {
            oldItem.setQuantity(updatedItemDto.getQuantity());
        }

        if (updatedItemDto.getPurchaseOrder() != null && updatedItemDto.getPurchaseOrder().getId() != null) {
            purchaseOrderRepository.findById(updatedItemDto.getPurchaseOrder().getId())
                    .ifPresent(oldItem::setPurchaseOrder);
        }

        return repository.save(oldItem);
    }

    public Page<Item> findAllPageable(int page, int size, String sortBy, String direction, ItemFilterDto itemFilter) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (Objects.nonNull(itemFilter.getProduct()) && Objects.nonNull(itemFilter.getProduct().getId())) {
            predicateBuilder.and(QItem.item.product.id.eq(itemFilter.getProduct().getId()));
        }

        if (Objects.nonNull(itemFilter.getId())) {
            predicateBuilder.and(QItem.item.id.eq(itemFilter.getId()));
        }

        if (Objects.nonNull(itemFilter.getPurchaseOrder()) && Objects.nonNull(itemFilter.getPurchaseOrder().getId())) {
            predicateBuilder.and(QItem.item.purchaseOrder.id.eq(itemFilter.getPurchaseOrder().getId()));
        }

        return itemDslRepository.findAll(predicateBuilder, pageable);
    }
}
