package com.modestoneto.pedidos.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modestoneto.pedidos.application.port.in.CreateUserUseCase;
import com.modestoneto.pedidos.application.port.in.DeleteUserUseCase;
import com.modestoneto.pedidos.application.port.in.FindUserByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindUsersUseCase;
import com.modestoneto.pedidos.application.port.in.UpdateUserUseCase;
import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.model.User;

@Service
public class UserService implements FindUsersUseCase, FindUserByIdUseCase, CreateUserUseCase,
		UpdateUserUseCase, DeleteUserUseCase {

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

	@Override
	public User create(User user) {
		user.setId(null);
		return userRepository.save(user);
	}

	@Override
	public Optional<User> update(Long id, User data) {
		return userRepository.findById(id).map(user -> {
			user.setName(data.getName());
			user.setEmail(data.getEmail());
			user.setPhone(data.getPhone());
			return userRepository.save(user);
		});
	}

	@Override
	public boolean delete(Long id) {
		if (userRepository.findById(id).isEmpty()) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}
}
