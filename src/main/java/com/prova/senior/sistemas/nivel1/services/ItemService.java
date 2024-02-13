package com.prova.senior.sistemas.nivel1.services;

import com.prova.senior.sistemas.nivel1.dtos.ItemDto;
import com.prova.senior.sistemas.nivel1.entities.Item;
import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.entities.PurchaseOrder;
import com.prova.senior.sistemas.nivel1.exceptions.ItemNotFoundException;
import com.prova.senior.sistemas.nivel1.exceptions.ProductNotFoundException;
import com.prova.senior.sistemas.nivel1.repositories.ItemRepository;
import com.prova.senior.sistemas.nivel1.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {
    private final ItemRepository repository;

    private final ProductRepository productRepository;

    public ItemService(ItemRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
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

        if (updatedItemDto.getPurchaseOrder() != null) {
            oldItem.setPurchaseOrder(updatedItemDto.getPurchaseOrder());
        }

        return repository.save(oldItem);
    }
}
