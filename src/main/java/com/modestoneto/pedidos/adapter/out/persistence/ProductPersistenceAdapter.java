package com.modestoneto.pedidos.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.application.port.out.ProductRepositoryPort;
import com.modestoneto.pedidos.domain.model.Product;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

	private final SpringDataProductRepository repository;
	private final ProductMapper mapper;

	public ProductPersistenceAdapter(SpringDataProductRepository repository, ProductMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<Product> findAll() {
		return repository.findAll().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Product> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	public Product save(Product product) {
		ProductJpaEntity saved = repository.save(mapper.toEntity(product));
		return mapper.toDomain(saved);
	}
}
