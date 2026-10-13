package com.modestoneto.pedidos.application.port.in;

import java.util.List;

import com.modestoneto.pedidos.domain.model.User;

public interface FindUsersUseCase {

	List<User> findAll();
}
