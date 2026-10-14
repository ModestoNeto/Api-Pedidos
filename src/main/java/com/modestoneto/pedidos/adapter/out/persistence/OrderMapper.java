package com.modestoneto.pedidos.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

@Component
public class OrderMapper {

	private final UserMapper userMapper;

	public OrderMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public Order toDomain(OrderJpaEntity entity) {
		return new Order(entity.getId(), entity.getMoment(), OrderStatus.valueOf(entity.getOrderStatus()),
				userMapper.toDomain(entity.getClient()));
	}

	public OrderJpaEntity toEntity(Order order) {
		return new OrderJpaEntity(order.getId(), order.getMoment(), order.getOrderStatus().getCode(),
				userMapper.toEntity(order.getClient()));
	}
}
