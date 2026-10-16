package com.modestoneto.pedidos.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modestoneto.pedidos.adapter.out.persistence.pk.OrderItemPK;

public interface SpringDataOrderItemRepository extends JpaRepository<OrderItemJpaEntity, OrderItemPK> {
}
