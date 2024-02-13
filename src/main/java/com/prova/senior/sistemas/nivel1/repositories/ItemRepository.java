package com.prova.senior.sistemas.nivel1.repositories;

import com.prova.senior.sistemas.nivel1.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
}
