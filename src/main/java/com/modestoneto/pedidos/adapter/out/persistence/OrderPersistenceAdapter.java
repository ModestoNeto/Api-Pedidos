package com.modestoneto.pedidos.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.OrderItem;

@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {

	private final SpringDataOrderRepository repository;
	private final SpringDataOrderItemRepository itemRepository;
	private final SpringDataProductRepository productRepository;
	private final OrderMapper mapper;

	public OrderPersistenceAdapter(SpringDataOrderRepository repository,
			SpringDataOrderItemRepository itemRepository, SpringDataProductRepository productRepository,
			OrderMapper mapper) {
		this.repository = repository;
		this.itemRepository = itemRepository;
		this.productRepository = productRepository;
		this.mapper = mapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findAll() {
		return repository.findAll().stream().map(mapper::toDomain).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Order> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	@Transactional
	public Order save(Order order) {
		OrderJpaEntity saved = repository.save(mapper.toEntity(order));
		for (OrderItem item : order.getItems()) {
			ProductJpaEntity product = productRepository.getReferenceById(item.getProduct().getId());
			OrderItemJpaEntity savedItem = itemRepository
					.save(new OrderItemJpaEntity(saved, product, item.getQuantity(), item.getPrice()));
			saved.getItems().add(savedItem);
		}
		return mapper.toDomain(saved);
	}
}
