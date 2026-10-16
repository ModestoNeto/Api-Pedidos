package com.modestoneto.pedidos.domain.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Instant moment;

	public Payment() {
	}

	public Payment(Long id, Instant moment) {
		this.id = id;
		this.moment = moment;
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
		Payment other = (Payment) obj;
		return Objects.equals(id, other.id);
	}
}
