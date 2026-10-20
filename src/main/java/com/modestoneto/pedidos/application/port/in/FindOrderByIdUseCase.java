package com.modestoneto.pedidos.application.port.in;

import com.modestoneto.pedidos.domain.model.Order;

public interface FindOrderByIdUseCase {

	Order findById(Long id);
}
