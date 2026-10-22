package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modestoneto.pedidos.adapter.in.web.exception.StandardError;
import com.modestoneto.pedidos.application.port.in.FindOrderByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindOrdersUseCase;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pedidos", description = "Consulta de pedidos, com cliente, itens, pagamento e total")
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final FindOrdersUseCase findOrders;
	private final FindOrderByIdUseCase findOrderById;

	public OrderController(FindOrdersUseCase findOrders, FindOrderByIdUseCase findOrderById) {
		this.findOrders = findOrders;
		this.findOrderById = findOrderById;
	}

	@Operation(summary = "Lista todos os pedidos")
	@GetMapping
	public ResponseEntity<List<OrderResponse>> findAll() {
		List<OrderResponse> list = findOrders.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@Operation(summary = "Busca um pedido por id",
			description = "Traz o cliente, os itens, o pagamento (nulo se o pedido não foi pago) e o total.")
	@ApiResponse(responseCode = "400", description = "Id com formato inválido",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@ApiResponse(responseCode = "404", description = "Pedido não encontrado",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(toResponse(findOrderById.findById(id)));
	}

	private OrderResponse toResponse(Order order) {
		User client = order.getClient();
		UserResponse clientResponse = new UserResponse(client.getId(), client.getName(), client.getEmail(),
				client.getPhone());
		List<OrderItemResponse> items = order.getItems().stream()
				.map(item -> new OrderItemResponse(item.getProduct().getId(), item.getProduct().getName(),
						item.getPrice(), item.getQuantity(), item.getSubTotal()))
				.toList();
		PaymentResponse payment = order.getPayment() == null ? null
				: new PaymentResponse(order.getPayment().getId(), order.getPayment().getMoment());
		return new OrderResponse(order.getId(), order.getMoment(), order.getOrderStatus().name(), clientResponse,
				items, payment, order.getTotal());
	}
}
