package com.modestoneto.pedidos.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modestoneto.pedidos.application.port.in.FindUserByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindUsersUseCase;
import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.model.User;

@Service
public class UserService implements FindUsersUseCase, FindUserByIdUseCase {

	private final UserRepositoryPort userRepository;

	public UserService(UserRepositoryPort userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
}
