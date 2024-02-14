package com.prova.senior.sistemas.nivel1.controllers;

import com.prova.senior.sistemas.nivel1.dtos.OrderStatusDto;
import com.prova.senior.sistemas.nivel1.dtos.PurchaseOrderDto;
import com.prova.senior.sistemas.nivel1.dtos.filters.PurchaseOrderFilterDto;
import com.prova.senior.sistemas.nivel1.entities.PurchaseOrder;
import com.prova.senior.sistemas.nivel1.services.PurchaseOrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class PurchaseOrderController {
    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> findAll() {
        List<PurchaseOrder> allOrders = service.findAll();
        return ResponseEntity.ok().body(allOrders);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PurchaseOrder>> findAll(
            @RequestBody PurchaseOrderFilterDto purchaseOrderFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok().body(service.findAllPageable(page, size, sortBy, direction, purchaseOrderFilter));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PurchaseOrder> create(@RequestBody PurchaseOrderDto newOrder) {
        PurchaseOrder order = service.create(newOrder);

        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PurchaseOrder> findOneById(@PathVariable UUID orderId) {
        PurchaseOrder order = service.findOneById(orderId);

        return ResponseEntity.ok().body(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(service.deleteById(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<PurchaseOrder> update(@PathVariable UUID orderId, @RequestBody PurchaseOrderDto updatedOrderDto) {
        return ResponseEntity.ok(service.update(orderId, updatedOrderDto));
    }

    @DeleteMapping("/item/{orderId}")
    public ResponseEntity<PurchaseOrder> removeItemsFromOneOrder(@PathVariable UUID orderId, @RequestBody List<UUID> itemIdsToRemove) {
        return ResponseEntity.ok(service.removeItemsFromOneOrder(orderId, itemIdsToRemove));
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable UUID orderId, @RequestBody OrderStatusDto newOrderStatus) {
        return ResponseEntity.ok(service.changeOrderStatus(orderId, newOrderStatus));
    }
}
