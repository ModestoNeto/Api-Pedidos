package com.modestoneto.pedidos.application.port.in;

import java.util.Optional;

import com.modestoneto.pedidos.domain.model.Order;

public interface FindOrderByIdUseCase {

	Optional<Order> findById(Long id);
}
