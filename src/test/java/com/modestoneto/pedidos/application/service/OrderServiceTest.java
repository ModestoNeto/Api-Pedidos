package com.modestoneto.pedidos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

class OrderServiceTest {

	private OrderRepositoryPort repository;
	private OrderService service;

	@BeforeEach
	void setUp() {
		repository = mock(OrderRepositoryPort.class);
		service = new OrderService(repository);
	}

	@Test
	void findAllDevolveATodosOsPedidos() {
		List<Order> orders = List.of(
				new Order(1L, Instant.parse("2026-10-20T12:00:00Z"), OrderStatus.PAID, null),
				new Order(2L, Instant.parse("2026-10-21T12:00:00Z"), OrderStatus.WAITING_PAYMENT, null));
		when(repository.findAll()).thenReturn(orders);

		assertEquals(orders, service.findAll());
	}

	@Test
	void findByIdDevolveOPedidoQuandoExiste() {
		Order order = new Order(1L, Instant.parse("2026-10-20T12:00:00Z"), OrderStatus.PAID, null);
		when(repository.findById(1L)).thenReturn(Optional.of(order));

		assertEquals(order, service.findById(1L));
	}

	@Test
	void findByIdLancaExcecaoQuandoNaoExiste() {
		when(repository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
	}
}
