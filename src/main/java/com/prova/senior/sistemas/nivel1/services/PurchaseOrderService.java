package com.prova.senior.sistemas.nivel1.services;

import com.prova.senior.sistemas.nivel1.dtos.OrderStatusDto;
import com.prova.senior.sistemas.nivel1.dtos.PurchaseOrderDto;
import com.prova.senior.sistemas.nivel1.dtos.filters.PurchaseOrderFilterDto;
import com.prova.senior.sistemas.nivel1.entities.*;
import com.prova.senior.sistemas.nivel1.enums.OrderStatusEnum;
import com.prova.senior.sistemas.nivel1.enums.Type;
import com.prova.senior.sistemas.nivel1.exceptions.*;
import com.prova.senior.sistemas.nivel1.repositories.ItemRepository;
import com.prova.senior.sistemas.nivel1.repositories.ProductRepository;
import com.prova.senior.sistemas.nivel1.repositories.PurchaseOrderDslRepository;
import com.prova.senior.sistemas.nivel1.repositories.PurchaseOrderRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository repository;

    private final ItemRepository itemRepository;

    private final ProductRepository productRepository;

    private final PurchaseOrderDslRepository purchaseOrderDslRepository;

    public PurchaseOrderService(PurchaseOrderRepository repository, ItemRepository itemRepository, ProductRepository productRepository, PurchaseOrderDslRepository purchaseOrderDslRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
        this.purchaseOrderDslRepository = purchaseOrderDslRepository;
    }

    public List<PurchaseOrder> findAll() {
        return repository.findAll();
    }

    @Transactional
    public PurchaseOrder create(PurchaseOrderDto newOrder) {
        if (newOrder.getItems().isEmpty()) {
            throw new RuntimeException("pedido sem itens");
        }

        List<Item> items = new ArrayList<>();
        List<String> productsNotActive = new ArrayList<>();

        for (Item itemDto : newOrder.getItems()) {
            Optional<Product> product = productRepository.findById(itemDto.getProduct().getId());

            if (product.isPresent()) {
                if (!product.get().isActive()) {
                    productsNotActive.add(String.format("Id: %s, Nome: %s",product.get().getId(), product.get().getName()));
                    continue;
                }

                Item item = new Item();

                item.setProduct(product.get());
                item.setQuantity(itemDto.getQuantity());
                item.setPurchaseOrder(null);

                items.add(item);
            }
        }

        if (!productsNotActive.isEmpty()) {
            throw new ProductsNotActiveOnOrderException(String.format("Produtos %s não estão ativos. Favor remover do pedido para a criação de um novo com produtos ativos", productsNotActive));
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
        PurchaseOrder oldOrder = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Nenhum pedido encontrado com ID " + orderId));

        List<Item> newItems = new ArrayList<>();

        for (Item itemDto : updatedOrderDto.getItems()) {
            Optional<Product> product = productRepository.findById(itemDto.getProduct().getId());

            if (product.isPresent()) {
                Item item = new Item();

                item.setProduct(product.get());
                item.setQuantity(itemDto.getQuantity());
                item.setPurchaseOrder(null);

                newItems.add(item);
            }
        }

        List<Item> itemsToRemove = new ArrayList<>();

        for (Item oldItem : oldOrder.getItems()) {
            for (Item newItem : newItems) {
                if (newItem.getId().equals(oldItem.getId())) {
                    itemsToRemove.add(oldItem);
                }
            }
        }

        oldOrder.getItems().removeAll(itemsToRemove);

        repository.saveAndFlush(oldOrder);

        for (Item item : newItems) {
            item.setPurchaseOrder(oldOrder);

            if (oldOrder.getOrderStatus().equals(OrderStatusEnum.OPEN)) {
                if (item.getProduct().getType().equals(Type.PRODUCT)) {
                    BigDecimal itemValueDiscount = item.getProduct().getPrice().multiply(oldOrder.getDiscount()
                            .divide(new BigDecimal("100"))
                            .multiply(new BigDecimal(item.getQuantity()))).setScale(2, RoundingMode.DOWN);

                    BigDecimal itemProductTotalValueMinusDiscount = item.getProduct().getPrice().subtract(itemValueDiscount);

                    BigDecimal totalValueWithItem = oldOrder.getTotalValue().add(itemProductTotalValueMinusDiscount);

                    oldOrder.setTotalValue(totalValueWithItem);
                } else {
                    BigDecimal totalValueWithoutAnyDiscount = oldOrder.getTotalValue().add(item.getProduct().getPrice());

                    oldOrder.setTotalValue(totalValueWithoutAnyDiscount);
                }
            }
        }

        List<Item> savedItems = itemRepository.saveAll(newItems);

        oldOrder.setItems(savedItems);

        if (updatedOrderDto.getDiscount() != null) {
            oldOrder.setDiscount(updatedOrderDto.getDiscount().setScale(2, RoundingMode.DOWN));
        }

        return repository.save(oldOrder);
    }

    public PurchaseOrder removeItemsFromOneOrder(UUID orderId, List<UUID> itemsIdsToRemove) {
        PurchaseOrder order = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Nenhum pedido encontrado com ID " + orderId));

        if (order.getItems().stream().map(Item::getId).noneMatch(itemsIdsToRemove::contains)) {
            throw new ItemsToRemoveDontExistsOnOrderException("Id dos itens informados não existem no pedido de Id " + orderId);
        }

        itemsIdsToRemove.forEach(itemId -> order.getItems().removeIf(item -> item.getId().equals(itemId)));

        return repository.save(order);
    }

    public Page<PurchaseOrder> findAllPageable(int page, int size, String sortBy, String direction, PurchaseOrderFilterDto purchaseOrderFilter) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (Objects.nonNull(purchaseOrderFilter.getId())) {
            predicateBuilder.and(QPurchaseOrder.purchaseOrder.id.eq(purchaseOrderFilter.getId()));
        }

        if (Objects.nonNull(purchaseOrderFilter.getOrderStatus()) && StringUtils.hasText(purchaseOrderFilter.getOrderStatus().name())) {
            predicateBuilder.and(QPurchaseOrder.purchaseOrder.orderStatus.eq(purchaseOrderFilter.getOrderStatus()));
        }

        return purchaseOrderDslRepository.findAll(predicateBuilder, pageable);
    }

    public String changeOrderStatus(UUID orderId, OrderStatusDto newOrderStatus) {
        PurchaseOrder order = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Nenhum pedido encontrado com ID " + orderId));

        if (Objects.isNull(newOrderStatus) || Objects.isNull(newOrderStatus.getOrderStatus())) {
            throw new OrderStatusNotInformedException("Novo status para alteração do pedido não informado");
        }

        if (newOrderStatus.getOrderStatus().equals(order.getOrderStatus())) {
            throw new OrderStatusSameException(String.format("Não é possível alterar para o mesmo status. Status atual: %s . Status desejado: %s", order.getOrderStatus(), newOrderStatus));
        }

        order.setOrderStatus(newOrderStatus.getOrderStatus());

        repository.save(order);

        return "Status alterado com sucesso";
    }
}
