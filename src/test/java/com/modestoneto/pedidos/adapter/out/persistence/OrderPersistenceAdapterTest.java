package com.modestoneto.pedidos.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
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

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@Import({ OrderPersistenceAdapter.class, OrderMapper.class, UserPersistenceAdapter.class, UserMapper.class,
		ProductPersistenceAdapter.class, ProductMapper.class, CategoryMapper.class })
class OrderPersistenceAdapterTest {

	@Autowired
	private OrderRepositoryPort orderRepository;

	@Autowired
	private UserRepositoryPort userRepository;

	@Autowired
	private ProductRepositoryPort productRepository;

	@Autowired
	private EntityManager em;

	private User client;
	private Product notebook;
	private Product book;

	@BeforeEach
	void setUp() {
		client = userRepository.save(new User(null, "Maria Exemplo", "maria@exemplo.com", "1", "senha-ficticia"));
		notebook = productRepository.save(new Product(null, "Notebook de Exemplo", "Descrição", 1250.0, ""));
		book = productRepository.save(new Product(null, "Livro de Exemplo", "Descrição", 90.5, ""));
	}

	private Order newOrder(OrderStatus status) {
		return new Order(null, Instant.parse("2026-10-21T12:00:00Z"), status, client);
	}

	@Test
	void gravaELeOPedidoComItensEmChaveComposta() {
		Order order = newOrder(OrderStatus.WAITING_PAYMENT);
		order.getItems().add(new OrderItem(book, 2, 90.5));
		order.getItems().add(new OrderItem(notebook, 1, 1250.0));

		Order saved = orderRepository.save(order);
		em.flush();
		em.clear();
		Order loaded = orderRepository.findById(saved.getId()).orElseThrow();

		assertEquals(2, loaded.getItems().size());
		assertEquals(1431.0, loaded.getTotal());
		assertTrue(loaded.getItems().stream().anyMatch(i -> i.getProduct().equals(book) && i.getQuantity() == 2));
		assertEquals(client, loaded.getClient());
		assertEquals(OrderStatus.WAITING_PAYMENT, loaded.getOrderStatus());
	}

	@Test
	void pedidoSemPagamentoVoltaComPagamentoNulo() {
		Order saved = orderRepository.save(newOrder(OrderStatus.WAITING_PAYMENT));
		em.flush();
		em.clear();

		assertNull(orderRepository.findById(saved.getId()).orElseThrow().getPayment());
	}

	@Test
	void gravaELeOPedidoComPagamentoUsandoOIdDoPedido() {
		Order order = newOrder(OrderStatus.PAID);
		order.setPayment(new Payment(null, Instant.parse("2026-10-21T14:00:00Z")));

		Order saved = orderRepository.save(order);
		em.flush();
		em.clear();
		Order loaded = orderRepository.findById(saved.getId()).orElseThrow();

		assertNotNull(loaded.getPayment());
		assertEquals(saved.getId(), loaded.getPayment().getId());
		assertEquals(Instant.parse("2026-10-21T14:00:00Z"), loaded.getPayment().getMoment());
	}

	@Test
	void salvarDeNovoUmPedidoJaPagoAtualizaOPagamento() {
		Order order = newOrder(OrderStatus.PAID);
		order.setPayment(new Payment(null, Instant.parse("2026-10-21T14:00:00Z")));
		Order saved = orderRepository.save(order);
		em.flush();
		em.clear();

		Order reloaded = orderRepository.findById(saved.getId()).orElseThrow();
		reloaded.setPayment(new Payment(reloaded.getId(), Instant.parse("2026-10-22T09:30:00Z")));
		orderRepository.save(reloaded);
		em.flush();
		em.clear();

		Order loaded = orderRepository.findById(saved.getId()).orElseThrow();
		assertEquals(Instant.parse("2026-10-22T09:30:00Z"), loaded.getPayment().getMoment());
	}

	@Test
	void findAllDevolveTodosOsPedidos() {
		orderRepository.save(newOrder(OrderStatus.PAID));
		orderRepository.save(newOrder(OrderStatus.SHIPPED));
		em.flush();
		em.clear();

		List<Order> all = orderRepository.findAll();

		assertEquals(2, all.size());
	}

	@Test
	void findByIdDevolveVazioQuandoNaoExiste() {
		assertTrue(orderRepository.findById(999L).isEmpty());
	}
}
