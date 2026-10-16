package com.modestoneto.pedidos.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

class OrderTest {

	@Test
	void totalEhASomaDosSubTotais() {
		Order order = new Order(1L, Instant.parse("2026-06-20T19:53:07Z"), OrderStatus.PAID, null);
		Product p1 = new Product(1L, "Produto A", "Descrição", 10.0, "");
		Product p2 = new Product(2L, "Produto B", "Descrição", 25.5, "");
		order.getItems().add(new OrderItem(p1, 2, 10.0));
		order.getItems().add(new OrderItem(p2, 1, 25.5));

		assertEquals(45.5, order.getTotal());
	}

	@Test
	void totalDePedidoSemItensEhZero() {
		Order order = new Order(1L, Instant.parse("2026-06-20T19:53:07Z"), OrderStatus.WAITING_PAYMENT, null);

		assertEquals(0.0, order.getTotal());
	}
}
