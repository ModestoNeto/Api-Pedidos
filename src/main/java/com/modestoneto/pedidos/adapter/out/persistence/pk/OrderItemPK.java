package com.modestoneto.pedidos.adapter.out.persistence.pk;

import java.io.Serializable;
import java.util.Objects;

import com.modestoneto.pedidos.adapter.out.persistence.OrderJpaEntity;
import com.modestoneto.pedidos.adapter.out.persistence.ProductJpaEntity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class OrderItemPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private OrderJpaEntity order;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductJpaEntity product;

	public OrderItemPK() {
	}

	public OrderJpaEntity getOrder() {
		return order;
	}

	public void setOrder(OrderJpaEntity order) {
		this.order = order;
	}

	public ProductJpaEntity getProduct() {
		return product;
	}

	public void setProduct(ProductJpaEntity product) {
		this.product = product;
	}

	@Override
	public int hashCode() {
		return Objects.hash(order, product);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		OrderItemPK other = (OrderItemPK) obj;
		return Objects.equals(order, other.order) && Objects.equals(product, other.product);
	}
}
