package com.modestoneto.pedidos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.model.User;

@Configuration
@Profile("test")
public class TestConfig {

	@Bean
	CommandLineRunner seedUsers(UserRepositoryPort userRepository) {
		return args -> {
			userRepository.save(new User(null, "Maria Exemplo", "maria@exemplo.com", "(00) 00000-0001", "senha-ficticia"));
			userRepository.save(new User(null, "Alex Exemplo", "alex@exemplo.com", "(00) 00000-0002", "senha-ficticia"));
			userRepository.save(new User(null, "Bruno Exemplo", "bruno@exemplo.com", "(00) 00000-0003", "senha-ficticia"));
		};
	}
}
