package com.prova.senior.sistemas.nivel1.services;

import com.prova.senior.sistemas.nivel1.dtos.PurchaseOrderDto;
import com.prova.senior.sistemas.nivel1.entities.Item;
import com.prova.senior.sistemas.nivel1.entities.Product;
import com.prova.senior.sistemas.nivel1.entities.PurchaseOrder;
import com.prova.senior.sistemas.nivel1.enums.Type;
import com.prova.senior.sistemas.nivel1.exceptions.OrderNotFoundException;
import com.prova.senior.sistemas.nivel1.repositories.ItemRepository;
import com.prova.senior.sistemas.nivel1.repositories.ProductRepository;
import com.prova.senior.sistemas.nivel1.repositories.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository repository;

    private final ItemRepository itemRepository;

    private final ProductRepository productRepository;

    public PurchaseOrderService(PurchaseOrderRepository repository, ItemRepository itemRepository, ItemService itemService, ProductRepository productRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
    }

    public List<PurchaseOrder> findAll() {
        return repository.findAll();
    }

    @Transactional
    public PurchaseOrder create(PurchaseOrderDto newOrder) {
        List<Item> items;

        if (newOrder.getItems().isEmpty()) {
            throw new RuntimeException("pedido sem itens");
        }

        items = new ArrayList<>();

        for (Item itemDto : newOrder.getItems()) {
            Optional<Product> product = productRepository.findById(itemDto.getProduct().getId());

            if (product.isPresent()) {
                Item item = new Item();

                item.setProduct(product.get());
                item.setQuantity(itemDto.getQuantity());
                item.setPurchaseOrder(null);

                items.add(item);
            }
        }

        newOrder.getItems().clear();

        PurchaseOrder purchaseOrder = new PurchaseOrder().convertToEntity(newOrder);

        repository.saveAndFlush(purchaseOrder);

        for (Item item : items) {
            item.setPurchaseOrder(purchaseOrder);

            if (item.getProduct().getType().equals(Type.PRODUCT)) {
                BigDecimal itemValueDiscount = item.getProduct().getPrice().multiply(purchaseOrder.getDiscount()
                        .divide(new BigDecimal("100"))
                        .multiply(new BigDecimal(item.getQuantity()))).setScale(2, RoundingMode.DOWN);

                BigDecimal itemProductTotalValueMinusDiscount = item.getProduct().getPrice().subtract(itemValueDiscount);

                BigDecimal totalValueWithItem = purchaseOrder.getTotalValue().add(itemProductTotalValueMinusDiscount);

                purchaseOrder.setTotalValue(totalValueWithItem);
            } else {
                BigDecimal totalValueWithoutAnyDiscount = purchaseOrder.getTotalValue().add(item.getProduct().getPrice());

                purchaseOrder.setTotalValue(totalValueWithoutAnyDiscount);
            }
        }

        List<Item> savedItems = itemRepository.saveAll(items);

        purchaseOrder.setItems(savedItems);

        return purchaseOrder;
    }

    public PurchaseOrder findOneById(UUID orderId) {
        return repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Nenhum pedido encontrado com ID " + orderId));
    }

    public String deleteById(UUID orderId) {
        PurchaseOrder orderToDelete = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Nenhum pedido encontrado com ID " + orderId));

        repository.delete(orderToDelete);

        return "Pedido removido com sucesso";
    }

    public PurchaseOrder update(UUID orderId, PurchaseOrderDto updatedOrderDto) {
        PurchaseOrder oldOder = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Nenhum pedido encontrado com ID " + orderId));

        if (!updatedOrderDto.getItems().isEmpty()) {
            Set<UUID> itemIds = updatedOrderDto.getItems().stream().map(Item::getId).collect(Collectors.toSet());

            List<Item> items = itemRepository.findAllById(itemIds);

//            oldOder.getItems().addAll(items);
        }

        if (updatedOrderDto.getDiscount() != null) {
            oldOder.setDiscount(updatedOrderDto.getDiscount().setScale(2, RoundingMode.DOWN));
        }

        return repository.save(oldOder);
    }

    public PurchaseOrder removeItemsFromOneOrder(UUID orderId, List<UUID> itemsIdsToRemove) {
        PurchaseOrder order = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Nenhum pedido encontrado com ID " + orderId));

//        if (order.getItems().stream().map(Item::getId).noneMatch(itemsIdsToRemove::contains)) {
//            throw new ItemsToRemoveDontExistsOnOrderException("Id dos itens informados não existem no pedido de Id " + orderId);
//        }
//
//        itemsIdsToRemove.forEach(itemId -> order.getItems().removeIf(item -> item.getId().equals(itemId)));

        return repository.save(order);
    }
}
