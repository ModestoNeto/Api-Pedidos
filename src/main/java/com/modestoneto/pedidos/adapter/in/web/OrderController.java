package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modestoneto.pedidos.application.port.in.FindOrderByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindOrdersUseCase;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.User;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final FindOrdersUseCase findOrders;
	private final FindOrderByIdUseCase findOrderById;

	public OrderController(FindOrdersUseCase findOrders, FindOrderByIdUseCase findOrderById) {
		this.findOrders = findOrders;
		this.findOrderById = findOrderById;
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> findAll() {
		List<OrderResponse> list = findOrders.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
		return findOrderById.findById(id)
				.map(order -> ResponseEntity.ok(toResponse(order)))
				.orElse(ResponseEntity.notFound().build());
	}

	private OrderResponse toResponse(Order order) {
		User client = order.getClient();
		UserResponse clientResponse = new UserResponse(client.getId(), client.getName(), client.getEmail(),
				client.getPhone());
		return new OrderResponse(order.getId(), order.getMoment(), order.getOrderStatus().name(), clientResponse);
	}
}
