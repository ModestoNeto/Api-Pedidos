package com.modestoneto.pedidos.application.port.in;

import java.util.List;

import com.modestoneto.pedidos.domain.model.Product;

public interface FindProductsUseCase {

	List<Product> findAll();
}
