package com.modestoneto.pedidos.adapter.out.persistence;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order")
public class OrderJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Instant moment;
	private Integer orderStatus;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private UserJpaEntity client;

	@OneToMany(mappedBy = "id.order")
	private Set<OrderItemJpaEntity> items = new HashSet<>();

	@OneToOne(mappedBy = "order")
	private PaymentJpaEntity payment;

	public OrderJpaEntity() {
	}

	public OrderJpaEntity(Long id, Instant moment, Integer orderStatus, UserJpaEntity client) {
		this.id = id;
		this.moment = moment;
		this.orderStatus = orderStatus;
		this.client = client;
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

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public UserJpaEntity getClient() {
		return client;
	}

	public void setClient(UserJpaEntity client) {
		this.client = client;
	}

	public Set<OrderItemJpaEntity> getItems() {
		return items;
	}

	public PaymentJpaEntity getPayment() {
		return payment;
	}

	public void setPayment(PaymentJpaEntity payment) {
		this.payment = payment;
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
		OrderJpaEntity other = (OrderJpaEntity) obj;
		return Objects.equals(id, other.id);
	}
}
