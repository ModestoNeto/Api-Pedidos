package com.modestoneto.pedidos.application.port.out;

import java.util.List;
import java.util.Optional;

import com.modestoneto.pedidos.domain.model.Product;

public interface ProductRepositoryPort {

	List<Product> findAll();

	Optional<Product> findById(Long id);

	Product save(Product product);
}
