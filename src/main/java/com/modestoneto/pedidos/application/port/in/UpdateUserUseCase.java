package com.modestoneto.pedidos.application.port.in;

import com.modestoneto.pedidos.domain.model.User;

public interface UpdateUserUseCase {

	User update(Long id, User data);
}
