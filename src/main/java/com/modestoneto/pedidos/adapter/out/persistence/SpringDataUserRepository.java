package com.modestoneto.pedidos.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {
}
