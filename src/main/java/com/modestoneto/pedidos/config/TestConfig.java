package com.modestoneto.pedidos.config;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.modestoneto.pedidos.application.port.out.CategoryRepositoryPort;
import com.modestoneto.pedidos.application.port.out.OrderRepositoryPort;
import com.modestoneto.pedidos.application.port.out.ProductRepositoryPort;
import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.model.Category;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.Product;
import com.modestoneto.pedidos.domain.model.User;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

@Configuration
@Profile("test")
public class TestConfig {

	@Bean
	CommandLineRunner seed(UserRepositoryPort userRepository, OrderRepositoryPort orderRepository,
			CategoryRepositoryPort categoryRepository, ProductRepositoryPort productRepository) {
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

			Category electronics = categoryRepository.save(new Category(null, "Eletrônicos"));
			Category books = categoryRepository.save(new Category(null, "Livros"));
			Category computers = categoryRepository.save(new Category(null, "Computadores"));

			Product p1 = new Product(null, "Livro de Exemplo", "Descrição de um livro fictício.", 90.5, "");
			p1.getCategories().add(books);
			productRepository.save(p1);

			Product p2 = new Product(null, "Smart TV de Exemplo", "Descrição de uma TV fictícia.", 2190.0, "");
			p2.getCategories().add(electronics);
			productRepository.save(p2);

			Product p3 = new Product(null, "Notebook de Exemplo", "Descrição de um notebook fictício.", 1250.0, "");
			p3.getCategories().add(computers);
			p3.getCategories().add(electronics);
			productRepository.save(p3);

			Product p4 = new Product(null, "PC Gamer de Exemplo", "Descrição de um PC fictício.", 1200.0, "");
			p4.getCategories().add(computers);
			productRepository.save(p4);
		};
	}
}
