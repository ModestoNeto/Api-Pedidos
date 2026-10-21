package com.modestoneto.pedidos.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.modestoneto.pedidos.application.port.out.CategoryRepositoryPort;
import com.modestoneto.pedidos.application.port.out.ProductRepositoryPort;
import com.modestoneto.pedidos.domain.model.Category;
import com.modestoneto.pedidos.domain.model.Product;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@Import({ ProductPersistenceAdapter.class, ProductMapper.class, CategoryPersistenceAdapter.class,
		CategoryMapper.class })
class ProductPersistenceAdapterTest {

	@Autowired
	private ProductRepositoryPort productRepository;

	@Autowired
	private CategoryRepositoryPort categoryRepository;

	@Autowired
	private EntityManager em;

	@Test
	void salvaProdutoComDuasCategoriasELeDeVolta() {
		Category electronics = categoryRepository.save(new Category(null, "Eletrônicos"));
		Category computers = categoryRepository.save(new Category(null, "Computadores"));
		Product product = new Product(null, "Notebook de Exemplo", "Descrição", 1250.0, "");
		product.getCategories().add(electronics);
		product.getCategories().add(computers);

		Product saved = productRepository.save(product);
		em.flush();
		em.clear();

		Product loaded = productRepository.findById(saved.getId()).orElseThrow();
		List<String> names = loaded.getCategories().stream().map(Category::getName).sorted().toList();
		assertEquals(List.of("Computadores", "Eletrônicos"), names);
		assertEquals(1250.0, loaded.getPrice());
	}

	@Test
	void umaCategoriaPodeEstarEmVariosProdutos() {
		Category books = categoryRepository.save(new Category(null, "Livros"));
		Product a = new Product(null, "Livro A", "Descrição", 10.0, "");
		Product b = new Product(null, "Livro B", "Descrição", 20.0, "");
		a.getCategories().add(books);
		b.getCategories().add(books);
		productRepository.save(a);
		productRepository.save(b);
		em.flush();
		em.clear();

		List<Product> all = productRepository.findAll();

		assertEquals(2, all.size());
		assertTrue(all.stream().allMatch(p -> p.getCategories().size() == 1));
	}

	@Test
	void findByIdDevolveVazioQuandoNaoExiste() {
		assertTrue(productRepository.findById(999L).isEmpty());
	}
}
