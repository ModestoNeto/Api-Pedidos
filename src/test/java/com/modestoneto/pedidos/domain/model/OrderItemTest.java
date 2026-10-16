package com.modestoneto.pedidos.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OrderItemTest {

	@Test
	void subTotalEhPrecoVezesQuantidade() {
		Product product = new Product(1L, "Produto de Exemplo", "Descrição", 10.0, "");
		OrderItem item = new OrderItem(product, 3, 10.0);

		assertEquals(30.0, item.getSubTotal());
	}
}
