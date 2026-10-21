package com.modestoneto.pedidos.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.modestoneto.pedidos.application.port.out.CategoryRepositoryPort;
import com.modestoneto.pedidos.domain.model.Category;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@Import({ CategoryPersistenceAdapter.class, CategoryMapper.class })
class CategoryPersistenceAdapterTest {

	@Autowired
	private CategoryRepositoryPort categoryRepository;

	@Autowired
	private EntityManager em;

	@Test
	void salvaELeCategoriaPorId() {
		Category saved = categoryRepository.save(new Category(null, "Livros"));
		assertNotNull(saved.getId());
		em.flush();
		em.clear();

		assertEquals("Livros", categoryRepository.findById(saved.getId()).orElseThrow().getName());
	}

	@Test
	void findByIdDevolveVazioQuandoNaoExiste() {
		assertTrue(categoryRepository.findById(999L).isEmpty());
	}

	@Test
	void findAllDevolveTodasAsCategorias() {
		categoryRepository.save(new Category(null, "Livros"));
		categoryRepository.save(new Category(null, "Eletrônicos"));
		em.flush();
		em.clear();

		assertEquals(2, categoryRepository.findAll().size());
	}
}
