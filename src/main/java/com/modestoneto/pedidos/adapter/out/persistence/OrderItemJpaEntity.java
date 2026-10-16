package com.modestoneto.pedidos.adapter.out.persistence;

import java.io.Serializable;
import java.util.Objects;

import com.modestoneto.pedidos.adapter.out.persistence.pk.OrderItemPK;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order_item")
public class OrderItemJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OrderItemPK id = new OrderItemPK();

	private Integer quantity;
	private Double price;

	public OrderItemJpaEntity() {
	}

	public OrderItemJpaEntity(OrderJpaEntity order, ProductJpaEntity product, Integer quantity, Double price) {
		id.setOrder(order);
		id.setProduct(product);
		this.quantity = quantity;
		this.price = price;
	}

	public OrderJpaEntity getOrder() {
		return id.getOrder();
	}

	public ProductJpaEntity getProduct() {
		return id.getProduct();
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		OrderItemJpaEntity other = (OrderItemJpaEntity) obj;
		return Objects.equals(id, other.id);
	}
}
