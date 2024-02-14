package com.prova.senior.sistemas.nivel1.repositories;

import com.prova.senior.sistemas.nivel1.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseOrderDslRepository extends JpaRepository<PurchaseOrder, UUID>, QuerydslPredicateExecutor<PurchaseOrder> {
}
