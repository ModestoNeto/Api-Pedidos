package com.modestoneto.pedidos.application.port.in;

import com.modestoneto.pedidos.domain.model.User;

public interface FindUserByIdUseCase {

	User findById(Long id);
}
