package com.modestoneto.pedidos.application.port.in;

import java.util.Optional;

import com.modestoneto.pedidos.domain.model.User;

public interface UpdateUserUseCase {

	Optional<User> update(Long id, User data);
}
