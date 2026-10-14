package com.modestoneto.pedidos.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.domain.model.Order;

@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {

	private final SpringDataOrderRepository repository;
	private final OrderMapper mapper;

	public OrderPersistenceAdapter(SpringDataOrderRepository repository, OrderMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<Order> findAll() {
		return repository.findAll().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Order> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	public Order save(Order order) {
		OrderJpaEntity saved = repository.save(mapper.toEntity(order));
		return mapper.toDomain(saved);
	}
}
