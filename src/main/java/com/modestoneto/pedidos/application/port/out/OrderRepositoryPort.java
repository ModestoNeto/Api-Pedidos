package com.modestoneto.pedidos.application.port.out;

import java.util.List;
import java.util.Optional;

import com.modestoneto.pedidos.domain.model.Order;

public interface OrderRepositoryPort {

	List<Order> findAll();

	Optional<Order> findById(Long id);

	Order save(Order order);
}
