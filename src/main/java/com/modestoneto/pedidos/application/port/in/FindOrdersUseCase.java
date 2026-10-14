package com.modestoneto.pedidos.application.port.in;

import java.util.List;

import com.modestoneto.pedidos.domain.model.Order;

public interface FindOrdersUseCase {

	List<Order> findAll();
}
