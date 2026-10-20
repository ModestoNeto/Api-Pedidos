package com.modestoneto.pedidos.application.port.in;

import com.modestoneto.pedidos.domain.model.User;

public interface CreateUserUseCase {

	User create(User user);
}
