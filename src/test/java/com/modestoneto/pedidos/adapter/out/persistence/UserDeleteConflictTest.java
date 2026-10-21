package com.modestoneto.pedidos.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ConflictException;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.User;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

/**
 * Fica como @SpringBootTest (sem transação de teste) porque a violação de chave
 * estrangeira só aparece no commit. Dentro da transação de um @DataJpaTest, o
 * deleteById não dispararia a exceção.
 */
@SpringBootTest
@ActiveProfiles("test")
class UserDeleteConflictTest {

	@Autowired
	private UserRepositoryPort userRepository;

	@Autowired
	private OrderRepositoryPort orderRepository;

	@Test
	void removerUsuarioComPedidosLancaConflito() {
		User user = userRepository
				.save(new User(null, "Cliente Com Pedido", "cliente.pedido@exemplo.com", "1", "senha-ficticia"));
		orderRepository.save(new Order(null, Instant.parse("2026-10-20T12:00:00Z"), OrderStatus.WAITING_PAYMENT, user));

		assertThrows(ConflictException.class, () -> userRepository.deleteById(user.getId()));
	}
}
