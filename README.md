# API de Pedidos

API REST de um e-commerce simplificado (usuários, pedidos, itens, produtos, categorias e pagamentos), feita com Spring Boot e JPA/Hibernate, organizada em **arquitetura hexagonal** (portas e adaptadores).

> Projeto de estudo. Este repositório é uma **versão adaptada** do projeto do curso "Java COMPLETO" (Udemy). Veja a seção [Origem e créditos](#origem-e-créditos).

## Status
Em desenvolvimento. Fase atual: itens do pedido e pagamento (Dia 5). Hoje a API lista e busca usuários, pedidos, produtos e categorias por id, lendo do H2 (perfil `test`, com dados de exemplo carregados na inicialização). Os pedidos trazem o cliente, o status, a data em ISO 8601, os itens, o pagamento (quando houver) e o total, e os produtos trazem suas categorias (associação muitos-para-muitos). O CRUD completo de usuários, o tratamento de erros e o banco MySQL chegam nas próximas fases.

Valores monetários usam `Double`, como no curso de referência. Isso é aceitável para estudo, mas em um sistema real o correto é `BigDecimal`, por causa de arredondamento.

## Stack
- Java 21
- Spring Boot 4.0.8 (Web MVC, Data JPA, Validation)
- Maven
- H2 (perfil `test`)
- MySQL 8 em Docker (perfil `dev`, previsto)
- Flyway (migrações, previsto)

## Arquitetura
Estrutura de pacotes em `com.modestoneto.pedidos`:

```
domain            núcleo: modelo e regras, sem Spring, JPA ou Jackson
application       portas de entrada (casos de uso), portas de saída e serviços
adapter.in.web    controllers REST e DTOs
adapter.out.persistence   entidades JPA, repositórios e adaptadores de persistência
config            configuração de beans e seed de dados de teste
```

Regra de dependência: o domínio não depende de nada; a aplicação depende só do domínio; os adaptadores dependem da aplicação e do domínio, nunca o contrário.

## Como rodar
Pré-requisitos: JDK 21 ou superior e Maven (o projeto inclui o Maven Wrapper).

```
.\mvnw.cmd spring-boot:run
```

O perfil `test` (H2 em memória) é o padrão por enquanto. A aplicação sobe em `http://localhost:8080`:

- `GET /users`: lista de usuários
- `GET /users/{id}`: busca um usuário por id (404 se não existir)
- `GET /orders`: lista de pedidos, com cliente, status, itens, pagamento e total
- `GET /orders/{id}`: busca um pedido por id, com itens, pagamento (`null` se não pago) e total (404 se não existir)
- `GET /products`: lista de produtos, com suas categorias
- `GET /products/{id}`: busca um produto por id (404 se não existir)
- `GET /categories`: lista de categorias
- `GET /categories/{id}`: busca uma categoria por id (404 se não existir)
- `/h2-console`: console do H2 (URL `jdbc:h2:mem:testdb`, usuário `sa`, senha vazia)

Para rodar os testes:

```
.\mvnw.cmd test
```

### Banco MySQL (Docker)
O `docker-compose.yml` na raiz sobe um MySQL 8 local na porta `3307`. As credenciais ficam em um arquivo `.env`, que **não** é versionado; use o `.env.example` como modelo. O Spring será ligado a esse banco na fase do perfil `dev`.

## Origem e créditos
Este projeto é de estudo e é baseado no curso **"Java COMPLETO"** (Udemy), cujo projeto de referência é uma API de pedidos com Spring Boot e JPA.

Esta é uma **versão adaptada**, não uma cópia do projeto do curso. Principais diferenças:
- **Arquitetura hexagonal** (portas e adaptadores), no lugar da arquitetura em camadas do curso.
- **Domínio separado da persistência:** o modelo de domínio não tem anotações de JPA; os mapeamentos ficam em classes próprias do adaptador de persistência.
- **DTOs** nas respostas e entradas da API, em vez de expor as entidades.
- **Spring Boot 4.x**, enquanto o curso foi gravado com Spring Boot 3.
- **MySQL em Docker e migrações com Flyway** no perfil de desenvolvimento.

O domínio (usuários, pedidos, itens, produtos, categorias e pagamentos) e os casos de uso seguem o do curso.
