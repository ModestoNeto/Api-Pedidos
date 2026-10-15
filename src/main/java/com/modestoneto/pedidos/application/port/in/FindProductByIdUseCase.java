package com.modestoneto.pedidos.application.port.in;

import java.util.Optional;

import com.modestoneto.pedidos.domain.model.Product;

public interface FindProductByIdUseCase {

	Optional<Product> findById(Long id);
}
