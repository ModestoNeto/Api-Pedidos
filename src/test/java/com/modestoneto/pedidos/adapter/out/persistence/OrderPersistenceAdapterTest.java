package com.modestoneto.pedidos.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.application.port.out.ProductRepositoryPort;
import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.OrderItem;
import com.modestoneto.pedidos.domain.model.Payment;
import com.modestoneto.pedidos.domain.model.Product;
import com.modestoneto.pedidos.domain.model.User;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

@SpringBootTest
@ActiveProfiles("test")
class OrderPersistenceAdapterTest {

	@Autowired
	private OrderRepositoryPort orderRepository;

	@Autowired
	private UserRepositoryPort userRepository;

	@Autowired
	private ProductRepositoryPort productRepository;

	@Test
	void gravaELeOPedidoComSeusItens() {
		User client = userRepository.findAll().get(0);
		List<Product> products = productRepository.findAll();

		Order order = new Order(null, Instant.parse("2026-10-16T12:00:00Z"), OrderStatus.WAITING_PAYMENT, client);
		order.getItems().add(new OrderItem(products.get(0), 2, 10.0));
		order.getItems().add(new OrderItem(products.get(1), 1, 25.5));

		Order saved = orderRepository.save(order);
		Order loaded = orderRepository.findById(saved.getId()).orElseThrow();

		assertEquals(2, loaded.getItems().size());
		assertEquals(45.5, loaded.getTotal());
		assertTrue(loaded.getItems().stream().anyMatch(i -> i.getProduct().equals(products.get(0))));
		assertNull(loaded.getPayment());
	}

	@Test
	void gravaELeOPedidoComPagamento() {
		User client = userRepository.findAll().get(0);

		Order order = new Order(null, Instant.parse("2026-10-16T12:00:00Z"), OrderStatus.PAID, client);
		order.setPayment(new Payment(null, Instant.parse("2026-10-16T14:00:00Z")));

		Order saved = orderRepository.save(order);
		Order loaded = orderRepository.findById(saved.getId()).orElseThrow();

		assertNotNull(loaded.getPayment());
		assertEquals(saved.getId(), loaded.getPayment().getId());
		assertEquals(Instant.parse("2026-10-16T14:00:00Z"), loaded.getPayment().getMoment());
	}
}
