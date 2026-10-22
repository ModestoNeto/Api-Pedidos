package com.modestoneto.pedidos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI pedidosOpenApi() {
		return new OpenAPI().info(new Info()
				.title("API de Pedidos")
				.description("API REST de um e-commerce simplificado (usuários, pedidos, itens, produtos, "
						+ "categorias e pagamentos), em arquitetura hexagonal. Projeto de estudo baseado no "
						+ "curso \"Java COMPLETO\" (Udemy), em versão adaptada.")
				.version("1.0.0"));
	}
}
