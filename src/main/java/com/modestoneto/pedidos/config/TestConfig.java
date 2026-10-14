package com.modestoneto.pedidos.config;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.User;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

@Configuration
@Profile("test")
public class TestConfig {

	@Bean
	CommandLineRunner seed(UserRepositoryPort userRepository, OrderRepositoryPort orderRepository) {
		return args -> {
			User maria = userRepository
					.save(new User(null, "Maria Exemplo", "maria@exemplo.com", "(00) 00000-0001", "senha-ficticia"));
			User alex = userRepository
					.save(new User(null, "Alex Exemplo", "alex@exemplo.com", "(00) 00000-0002", "senha-ficticia"));
			userRepository
					.save(new User(null, "Bruno Exemplo", "bruno@exemplo.com", "(00) 00000-0003", "senha-ficticia"));

			orderRepository.save(new Order(null, Instant.parse("2026-06-20T19:53:07Z"), OrderStatus.PAID, maria));
			orderRepository
					.save(new Order(null, Instant.parse("2026-07-21T03:42:10Z"), OrderStatus.WAITING_PAYMENT, alex));
			orderRepository.save(new Order(null, Instant.parse("2026-07-22T15:21:22Z"), OrderStatus.SHIPPED, maria));
		};
	}
}
