package com.modestoneto.pedidos.domain.model.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OrderStatusTest {

	@Test
	void codigosValidosRetornamConstanteCorreta() {
		assertEquals(OrderStatus.WAITING_PAYMENT, OrderStatus.valueOf(1));
		assertEquals(OrderStatus.PAID, OrderStatus.valueOf(2));
		assertEquals(OrderStatus.SHIPPED, OrderStatus.valueOf(3));
		assertEquals(OrderStatus.DELIVERED, OrderStatus.valueOf(4));
		assertEquals(OrderStatus.CANCELED, OrderStatus.valueOf(5));

		for (int code = 1; code <= 5; code++) {
			assertEquals(code, OrderStatus.valueOf(code).getCode());
		}
	}

	@Test
	void codigoInvalidoLancaIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf(0));
		assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf(6));
	}
}
