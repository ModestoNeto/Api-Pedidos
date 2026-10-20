package com.modestoneto.pedidos.adapter.in.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.modestoneto.pedidos.application.port.in.CreateUserUseCase;
import com.modestoneto.pedidos.application.port.in.FindUserByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindUsersUseCase;
import com.modestoneto.pedidos.domain.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

	private final FindUsersUseCase findUsers;
	private final FindUserByIdUseCase findUserById;
	private final CreateUserUseCase createUser;

	public UserController(FindUsersUseCase findUsers, FindUserByIdUseCase findUserById,
			CreateUserUseCase createUser) {
		this.findUsers = findUsers;
		this.findUserById = findUserById;
		this.createUser = createUser;
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

	@PostMapping
	public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
		User user = new User(null, request.name(), request.email(), request.phone(), request.password());
		User created = createUser.create(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId())
				.toUri();
		return ResponseEntity.created(uri).body(toResponse(created));
	}

	private UserResponse toResponse(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
	}
}
