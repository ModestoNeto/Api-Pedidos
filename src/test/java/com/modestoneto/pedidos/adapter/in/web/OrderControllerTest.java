package com.modestoneto.pedidos.adapter.in.web;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.modestoneto.pedidos.application.port.in.FindOrderByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindOrdersUseCase;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Order;
import com.modestoneto.pedidos.domain.model.OrderItem;
import com.modestoneto.pedidos.domain.model.Payment;
import com.modestoneto.pedidos.domain.model.Product;
import com.modestoneto.pedidos.domain.model.User;
import com.modestoneto.pedidos.domain.model.enums.OrderStatus;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private FindOrdersUseCase findOrders;

	@MockitoBean
	private FindOrderByIdUseCase findOrderById;

	private final User client = new User(1L, "Maria Exemplo", "maria@exemplo.com", "1", "senha-ficticia");

	private Order paidOrder() {
		Order order = new Order(1L, Instant.parse("2026-06-20T19:53:07Z"), OrderStatus.PAID, client);
		order.getItems().add(new OrderItem(new Product(1L, "Livro de Exemplo", "Descrição", 90.5, ""), 2, 90.5));
		order.getItems().add(new OrderItem(new Product(3L, "Notebook de Exemplo", "Descrição", 1250.0, ""), 1, 1250.0));
		order.setPayment(new Payment(1L, Instant.parse("2026-06-20T21:53:07Z")));
		return order;
	}

	@Test
	void buscaPedidoComClienteItensPagamentoETotal() throws Exception {
		when(findOrderById.findById(1L)).thenReturn(paidOrder());

		mvc.perform(get("/orders/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.moment").value("2026-06-20T19:53:07Z"))
				.andExpect(jsonPath("$.orderStatus").value("PAID"))
				.andExpect(jsonPath("$.client.name").value("Maria Exemplo"))
				.andExpect(jsonPath("$.client.password").doesNotExist())
				.andExpect(jsonPath("$.items.length()").value(2))
				.andExpect(jsonPath("$.items[0].productId").value(1))
				.andExpect(jsonPath("$.items[0].subTotal").value(181.0))
				.andExpect(jsonPath("$.payment.moment").value("2026-06-20T21:53:07Z"))
				.andExpect(jsonPath("$.total").value(1431.0));
	}

	@Test
	void pedidoNaoPagoTemPagamentoNulo() throws Exception {
		Order order = new Order(2L, Instant.parse("2026-07-21T03:42:10Z"), OrderStatus.WAITING_PAYMENT, client);
		order.getItems().add(new OrderItem(new Product(3L, "Notebook de Exemplo", "Descrição", 1250.0, ""), 2, 1250.0));
		when(findOrderById.findById(2L)).thenReturn(order);

		mvc.perform(get("/orders/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderStatus").value("WAITING_PAYMENT"))
				.andExpect(jsonPath("$.payment").value(nullValue()))
				.andExpect(jsonPath("$.total").value(2500.0));
	}

	@Test
	void listaOsPedidos() throws Exception {
		when(findOrders.findAll()).thenReturn(List.of(paidOrder()));

		mvc.perform(get("/orders"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].total").value(1431.0));
	}

	@Test
	void idInexistenteRetorna404NoFormatoPadrao() throws Exception {
		when(findOrderById.findById(999L)).thenThrow(new ResourceNotFoundException("Pedido não encontrado: id 999"));

		mvc.perform(get("/orders/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Recurso não encontrado"))
				.andExpect(jsonPath("$.message").value("Pedido não encontrado: id 999"))
				.andExpect(jsonPath("$.path").value("/orders/999"));
	}

	@Test
	void idComTipoErradoRetorna400() throws Exception {
		mvc.perform(get("/orders/abc"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Requisição inválida"));
	}
}
