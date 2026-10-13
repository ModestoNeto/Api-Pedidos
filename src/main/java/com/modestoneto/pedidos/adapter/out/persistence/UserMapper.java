package com.modestoneto.pedidos.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.domain.model.User;

@Component
public class UserMapper {

	public User toDomain(UserJpaEntity entity) {
		return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getPassword());
	}

	public UserJpaEntity toEntity(User user) {
		return new UserJpaEntity(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getPassword());
	}
}
