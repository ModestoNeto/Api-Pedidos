package com.modestoneto.pedidos.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modestoneto.pedidos.application.port.in.FindOrderByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindOrdersUseCase;
import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.domain.model.Order;

@Service
public class OrderService implements FindOrdersUseCase, FindOrderByIdUseCase {

	private final OrderRepositoryPort orderRepository;

	public OrderService(OrderRepositoryPort orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}
}
