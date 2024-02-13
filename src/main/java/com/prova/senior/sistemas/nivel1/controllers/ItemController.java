package com.prova.senior.sistemas.nivel1.controllers;

import com.prova.senior.sistemas.nivel1.dtos.ItemDto;
import com.prova.senior.sistemas.nivel1.entities.Item;
import com.prova.senior.sistemas.nivel1.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        List<Item> allItems = service.findAll();
        return ResponseEntity.ok().body(allItems);
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody ItemDto newItem) {
        Item item = service.create(newItem);

        return ResponseEntity.ok().body(item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> findOneById(@PathVariable UUID itemId) {
        Item item = service.findOneById(itemId);

        return ResponseEntity.ok().body(item);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteById(@PathVariable UUID itemId) {
        return ResponseEntity.ok(service.deleteById(itemId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> update(@PathVariable UUID itemId, @RequestBody ItemDto updatedItemDto) {
        return ResponseEntity.ok(service.update(itemId, updatedItemDto));
    }
}
