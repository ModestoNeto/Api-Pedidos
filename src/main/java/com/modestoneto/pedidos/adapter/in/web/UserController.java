package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modestoneto.pedidos.application.port.in.FindUserByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindUsersUseCase;
import com.modestoneto.pedidos.domain.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

	private final FindUsersUseCase findUsers;
	private final FindUserByIdUseCase findUserById;

	public UserController(FindUsersUseCase findUsers, FindUserByIdUseCase findUserById) {
		this.findUsers = findUsers;
		this.findUserById = findUserById;
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> findAll() {
		List<UserResponse> list = findUsers.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
		return findUserById.findById(id)
				.map(user -> ResponseEntity.ok(toResponse(user)))
				.orElse(ResponseEntity.notFound().build());
	}

	private UserResponse toResponse(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
	}
}
