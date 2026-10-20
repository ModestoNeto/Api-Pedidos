package com.modestoneto.pedidos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.User;

class UserServiceTest {

	private UserRepositoryPort repository;
	private UserService service;

	@BeforeEach
	void setUp() {
		repository = mock(UserRepositoryPort.class);
		service = new UserService(repository);
	}

	@Test
	void findByIdDevolveOUsuarioQuandoExiste() {
		User user = new User(1L, "Maria Exemplo", "maria@exemplo.com", "1", "senha-ficticia");
		when(repository.findById(1L)).thenReturn(Optional.of(user));

		assertEquals(user, service.findById(1L));
	}

	@Test
	void findByIdLancaExcecaoQuandoNaoExiste() {
		when(repository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
	}

	@Test
	void updateLancaExcecaoQuandoNaoExiste() {
		when(repository.findById(999L)).thenReturn(Optional.empty());
		User data = new User(null, "Novo Nome", "novo@exemplo.com", "2", null);

		assertThrows(ResourceNotFoundException.class, () -> service.update(999L, data));
		verify(repository, never()).save(any());
	}

	@Test
	void deleteLancaExcecaoQuandoNaoExisteESemApagar() {
		when(repository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.delete(999L));
		verify(repository, never()).deleteById(any());
	}
}
