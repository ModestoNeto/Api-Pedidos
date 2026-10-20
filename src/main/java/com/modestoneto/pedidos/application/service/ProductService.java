package com.modestoneto.pedidos.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.modestoneto.pedidos.application.port.in.FindProductByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindProductsUseCase;
import com.modestoneto.pedidos.application.port.out.ProductRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Product;

@Service
public class ProductService implements FindProductsUseCase, FindProductByIdUseCase {

	private final ProductRepositoryPort productRepository;

	public ProductService(ProductRepositoryPort productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public Product findById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: id " + id));
	}
}
