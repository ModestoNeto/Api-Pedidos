# API de Pedidos

API REST de um e-commerce simplificado (usuários, pedidos, itens, produtos, categorias e pagamentos), feita com Spring Boot e JPA/Hibernate, organizada em **arquitetura hexagonal** (portas e adaptadores).

> Projeto de estudo. Este repositório é uma **versão adaptada** do projeto do curso "Java COMPLETO" (Udemy). Veja a seção [Origem e créditos](#origem-e-créditos).

## Status
Em desenvolvimento. Fase atual: banco MySQL e migrações (Dia 8). Hoje a API lista e busca usuários, pedidos, produtos e categorias por id, lendo de um MySQL em Docker, com o esquema e os dados de exemplo criados por migrações do Flyway (o perfil `test` usa H2 em memória, para rodar sem Docker). Os usuários podem ser criados, atualizados e removidos, com validação da entrada. Os pedidos trazem o cliente, o status, a data em ISO 8601, os itens, o pagamento (quando houver) e o total, e os produtos trazem suas categorias (associação muitos-para-muitos). Os erros seguem um formato único, com os códigos HTTP corretos (400, 404 e 409). Os testes automatizados de mais camadas e a documentação OpenAPI chegam nas próximas fases.

### Limitações conhecidas
- **Valores monetários** usam `Double`, como no curso de referência. Isso é aceitável para estudo, mas em um sistema real o correto é `BigDecimal`, por causa de arredondamento.
- **Senhas** são gravadas em texto puro. O projeto não usa Spring Security nem criptografia, e a senha nunca é devolvida pela API. Em um sistema real seria obrigatório gravar apenas um hash.
- **Erros inesperados** (por exemplo, uma falha de banco que não seja de integridade) ainda caem no tratamento padrão do Spring, com resposta 500, e não no formato próprio descrito abaixo.

## Stack
- Java 21
- Spring Boot 4.0.8 (Web MVC, Data JPA, Validation)
- Maven
- MySQL 8 em Docker (padrão)
- Flyway (migrações do esquema e dos dados de exemplo)
- H2 (perfil `test`, usado nos testes e para rodar sem Docker)
- JUnit 5 e Mockito

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
Pré-requisitos: JDK 21 ou superior, Docker (com Docker Compose) e Maven (o projeto inclui o Maven Wrapper).

1. Crie o arquivo `.env` na raiz a partir do modelo `.env.example` e troque os valores por senhas suas. O `.env` **não** é versionado.
2. Suba o banco: `docker compose up -d --wait`.
3. Suba a aplicação:

```
.\mvnw.cmd spring-boot:run
```

Por padrão a aplicação usa o **MySQL do Docker** (porta `3307`). Na primeira subida, o Flyway cria as tabelas e carrega os dados de exemplo. O Spring lê o `.env` sozinho, então não é preciso exportar variáveis.

Para rodar **sem Docker**, use o perfil `test`, que troca o banco por um H2 em memória (com os mesmos dados de exemplo, carregados por código):

```
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=test"
```

A aplicação sobe em `http://localhost:8080`:

- `GET /users`: lista de usuários
- `GET /users/{id}`: busca um usuário por id (404 se não existir)
- `POST /users`: cria um usuário (201, com o header `Location`; 400 se `name`, `email` ou `password` forem inválidos)
- `PUT /users/{id}`: atualiza `name`, `email` e `phone` (a senha não muda; 404 se não existir; 400 se inválido)
- `DELETE /users/{id}`: remove um usuário (204; 404 se não existir; 409 se o usuário tiver pedidos)
- `GET /orders`: lista de pedidos, com cliente, status, itens, pagamento e total
- `GET /orders/{id}`: busca um pedido por id, com itens, pagamento (`null` se não pago) e total (404 se não existir)
- `GET /products`: lista de produtos, com suas categorias
- `GET /products/{id}`: busca um produto por id (404 se não existir)
- `GET /categories`: lista de categorias
- `GET /categories/{id}`: busca uma categoria por id (404 se não existir)
- `/h2-console`: console do H2, só no perfil `test` (URL `jdbc:h2:mem:testdb`, usuário `sa`, senha vazia)

### Formato dos erros
Todos os erros tratados voltam no mesmo formato:

```json
{
  "timestamp": "2026-10-20T18:56:52Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Usuário não encontrado: id 999",
  "path": "/users/999"
}
```

| Status | Quando acontece |
|---|---|
| 400 | Dados de entrada inválidos, corpo da requisição ilegível ou id com tipo errado (por exemplo `/users/abc`) |
| 404 | O id pedido não existe |
| 409 | Remover um usuário que tem pedidos |

As exceções (`ResourceNotFoundException` e `ConflictException`) ficam no domínio e não conhecem HTTP. Um `@ControllerAdvice` no adaptador web as traduz para os códigos acima, e o adaptador de persistência converte a violação de integridade do banco em `ConflictException`.

Para rodar os testes (não precisam de Docker, porque usam o perfil `test` com H2):

```
.\mvnw.cmd test
```

### Banco de dados
- **Contêiner:** o `docker-compose.yml` na raiz sobe um MySQL 8 local, publicado só em `127.0.0.1:3307`. Comandos do dia a dia: `docker compose up -d --wait`, `docker compose ps`, `docker compose down` (mantém os dados) e `docker compose down -v` (apaga os dados).
- **Credenciais:** ficam no `.env` (`DB_NAME`, `DB_USER`, `DB_PASSWORD` e `DB_ROOT_PASSWORD`). Nenhuma senha está no repositório. Se você mudar a senha no `.env` depois que o banco foi criado, é preciso recriar o volume com `docker compose down -v`.
- **Migrações (Flyway):** o esquema e os dados de exemplo vêm de `src/main/resources/db/migration` (`V1__schema.sql` e `V2__seed.sql`), e o Hibernate roda com `ddl-auto=validate`, só conferindo se as entidades combinam com as tabelas. **Migração já aplicada não deve ser editada** (o Flyway guarda um checksum); para mudar o esquema, crie uma nova (`V3__...`). Em desenvolvimento, `docker compose down -v` recomeça do zero.
- **Dois bancos, dois perfis:** o padrão usa MySQL com Flyway; o perfil `test` usa H2 em memória, com `ddl-auto=create-drop` e os dados carregados por código (`TestConfig`).
- **Só um adaptador muda:** trocar de H2 para MySQL alterou apenas a configuração e o adaptador de persistência. O domínio e a camada de aplicação não foram tocados, que é o ponto da arquitetura hexagonal.

## Origem e créditos
Este projeto é de estudo e é baseado no curso **"Java COMPLETO"** (Udemy), cujo projeto de referência é uma API de pedidos com Spring Boot e JPA.

Esta é uma **versão adaptada**, não uma cópia do projeto do curso. Principais diferenças:
- **Arquitetura hexagonal** (portas e adaptadores), no lugar da arquitetura em camadas do curso.
- **Domínio separado da persistência:** o modelo de domínio não tem anotações de JPA; os mapeamentos ficam em classes próprias do adaptador de persistência.
- **DTOs** nas respostas e entradas da API, em vez de expor as entidades.
- **Spring Boot 4.x**, enquanto o curso foi gravado com Spring Boot 3.
- **MySQL em Docker e migrações com Flyway** no perfil de desenvolvimento.

O domínio (usuários, pedidos, itens, produtos, categorias e pagamentos) e os casos de uso seguem o do curso.
