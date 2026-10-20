package com.modestoneto.pedidos.application.port.in;

import com.modestoneto.pedidos.domain.model.Product;

public interface FindProductByIdUseCase {

	Product findById(Long id);
}
