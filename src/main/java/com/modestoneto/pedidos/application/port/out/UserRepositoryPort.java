package com.modestoneto.pedidos.application.port.out;

import java.util.List;
import java.util.Optional;

import com.modestoneto.pedidos.domain.model.User;

public interface UserRepositoryPort {

	List<User> findAll();

	Optional<User> findById(Long id);

	User save(User user);

	void deleteById(Long id);
}
