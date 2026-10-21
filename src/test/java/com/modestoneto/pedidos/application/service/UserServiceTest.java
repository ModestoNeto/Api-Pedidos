package com.modestoneto.pedidos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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
	void findAllDevolveATodosOsUsuarios() {
		List<User> users = List.of(
				new User(1L, "Maria Exemplo", "maria@exemplo.com", "1", "senha-ficticia"),
				new User(2L, "Alex Exemplo", "alex@exemplo.com", "2", "senha-ficticia"));
		when(repository.findAll()).thenReturn(users);

		assertEquals(users, service.findAll());
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
	void createSempreEnviaIdNuloParaOBancoGerar() {
		User input = new User(42L, "Carla Exemplo", "carla@exemplo.com", "3", "senha-ficticia");
		when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		service.create(input);

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(repository).save(captor.capture());
		assertNull(captor.getValue().getId());
	}

	@Test
	void createDevolveOUsuarioSalvo() {
		User saved = new User(4L, "Carla Exemplo", "carla@exemplo.com", "3", "senha-ficticia");
		when(repository.save(any(User.class))).thenReturn(saved);

		assertEquals(saved, service.create(new User(null, "Carla Exemplo", "carla@exemplo.com", "3", "senha-ficticia")));
	}

	@Test
	void updateAlteraNomeEmailETelefoneMasNaoASenha() {
		User existing = new User(1L, "Maria Exemplo", "maria@exemplo.com", "1", "senha-ficticia");
		when(repository.findById(1L)).thenReturn(Optional.of(existing));
		when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		User data = new User(null, "Novo Nome", "novo@exemplo.com", "9", "outra-senha");

		User updated = service.update(1L, data);

		assertEquals("Novo Nome", updated.getName());
		assertEquals("novo@exemplo.com", updated.getEmail());
		assertEquals("9", updated.getPhone());
		assertEquals("senha-ficticia", updated.getPassword());
		assertEquals(1L, updated.getId());
	}

	@Test
	void updateLancaExcecaoQuandoNaoExiste() {
		when(repository.findById(999L)).thenReturn(Optional.empty());
		User data = new User(null, "Novo Nome", "novo@exemplo.com", "2", null);

		assertThrows(ResourceNotFoundException.class, () -> service.update(999L, data));
		verify(repository, never()).save(any());
	}

	@Test
	void deleteApagaQuandoExiste() {
		User existing = new User(3L, "Bruno Exemplo", "bruno@exemplo.com", "3", "senha-ficticia");
		when(repository.findById(3L)).thenReturn(Optional.of(existing));

		service.delete(3L);

		verify(repository).deleteById(3L);
	}

	@Test
	void deleteLancaExcecaoQuandoNaoExisteESemApagar() {
		when(repository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.delete(999L));
		verify(repository, never()).deleteById(any());
	}
}
