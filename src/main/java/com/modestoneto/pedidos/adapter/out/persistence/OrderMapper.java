package com.modestoneto.pedidos.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.OrderItem;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

@Component
public class OrderMapper {

	private final UserMapper userMapper;
	private final ProductMapper productMapper;

	public OrderMapper(UserMapper userMapper, ProductMapper productMapper) {
		this.userMapper = userMapper;
		this.productMapper = productMapper;
	}

	public Order toDomain(OrderJpaEntity entity) {
		Order order = new Order(entity.getId(), entity.getMoment(), OrderStatus.valueOf(entity.getOrderStatus()),
				userMapper.toDomain(entity.getClient()));
		for (OrderItemJpaEntity item : entity.getItems()) {
			order.getItems()
					.add(new OrderItem(productMapper.toDomain(item.getProduct()), item.getQuantity(), item.getPrice()));
		}
		return order;
	}

	/**
	 * Converte só os dados do pedido. Os itens são gravados à parte pelo adaptador,
	 * depois que o pedido tem id.
	 */
	public OrderJpaEntity toEntity(Order order) {
		return new OrderJpaEntity(order.getId(), order.getMoment(), order.getOrderStatus().getCode(),
				userMapper.toEntity(order.getClient()));
	}
}
