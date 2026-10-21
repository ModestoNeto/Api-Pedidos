package com.modestoneto.pedidos.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.model.User;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@Import({ UserPersistenceAdapter.class, UserMapper.class })
class UserPersistenceAdapterTest {

	@Autowired
	private UserRepositoryPort userRepository;

	@Autowired
	private EntityManager em;

	private User newUser(String name, String email) {
		return new User(null, name, email, "(00) 00000-0000", "senha-ficticia");
	}

	@Test
	void salvaELeUsuarioPorId() {
		User saved = userRepository.save(newUser("Maria Exemplo", "maria@exemplo.com"));
		assertNotNull(saved.getId());
		em.flush();
		em.clear();

		User loaded = userRepository.findById(saved.getId()).orElseThrow();

		assertEquals("Maria Exemplo", loaded.getName());
		assertEquals("maria@exemplo.com", loaded.getEmail());
		assertEquals("senha-ficticia", loaded.getPassword());
	}

	@Test
	void findByIdDevolveVazioQuandoNaoExiste() {
		assertTrue(userRepository.findById(999L).isEmpty());
	}

	@Test
	void findAllDevolveTodosOsUsuarios() {
		userRepository.save(newUser("Maria Exemplo", "maria@exemplo.com"));
		userRepository.save(newUser("Alex Exemplo", "alex@exemplo.com"));
		em.flush();
		em.clear();

		assertEquals(2, userRepository.findAll().size());
	}

	@Test
	void deleteByIdRemoveUsuarioSemPedidos() {
		User saved = userRepository.save(newUser("Bruno Exemplo", "bruno@exemplo.com"));
		em.flush();

		userRepository.deleteById(saved.getId());
		em.flush();
		em.clear();

		assertTrue(userRepository.findById(saved.getId()).isEmpty());
	}
}
