package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@GetMapping
	public ResponseEntity<List<UserResponse>> findAll() {
		UserResponse u = new UserResponse(1L, "Modesto Neto", "modesto@exemplo.com", "(00) 00000-0000");
		return ResponseEntity.ok(List.of(u));
	}
}
