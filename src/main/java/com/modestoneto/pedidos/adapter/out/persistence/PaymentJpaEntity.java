package com.modestoneto.pedidos.adapter.out.persistence;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_payment")
public class PaymentJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private Instant moment;

	@OneToOne
	@MapsId
	@JoinColumn(name = "order_id")
	private OrderJpaEntity order;

	public PaymentJpaEntity() {
	}

	public PaymentJpaEntity(Long id, Instant moment, OrderJpaEntity order) {
		this.id = id;
		this.moment = moment;
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public OrderJpaEntity getOrder() {
		return order;
	}

	public void setOrder(OrderJpaEntity order) {
		this.order = order;
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
		PaymentJpaEntity other = (PaymentJpaEntity) obj;
		return Objects.equals(id, other.id);
	}
}
