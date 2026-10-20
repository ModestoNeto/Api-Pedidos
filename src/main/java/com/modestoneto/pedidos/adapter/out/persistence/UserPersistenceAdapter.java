package com.modestoneto.pedidos.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.application.port.out.UserRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ConflictException;
import com.modestoneto.pedidos.domain.model.User;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

	private final SpringDataUserRepository repository;
	private final UserMapper mapper;

	public UserPersistenceAdapter(SpringDataUserRepository repository, UserMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<User> findAll() {
		return repository.findAll().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<User> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	public User save(User user) {
		UserJpaEntity saved = repository.save(mapper.toEntity(user));
		return mapper.toDomain(saved);
	}

	@Override
	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException("Não é possível remover o usuário: ele possui pedidos");
		}
	}
}
