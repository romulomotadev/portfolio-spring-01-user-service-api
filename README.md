![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3-green)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
---

# 👤 User Service API

## 📌 Sobre o projeto

Microsserviço de autenticação e gerenciamento de usuários baseado em arquitetura de microsserviços com JWT e OAuth2.

---

## ⚡ Quick Start

```bash
git clone https://github.com/romulomotadev/portfolio-spring-01-user-service-api.git
cd portfolio-spring-01-user-service-api
docker compose up --build
```

---

## 🛠️ Tecnologias
* **Linguagem:** Java 21
* **Framework:** Spring Boot 3, Spring Data JPA, Spring Security
* **Segurança:** Servidor de autorização OAuth2 com tokens JWT (Servidor de Autorização Spring)
* **Banco de Dados:** PostgreSQL (Produção/Dev) e H2 (Testes)
* **APIs:** REST, Swagger/OpenAPI
* **Testes:** JUnit 5, Mockito
* **Infraestrutura:** Docker / Docker Compose
* **Build:** Maven

---

## ✨ Principais Recursos

- Authorization & Authentication (JWT / OAuth2)
- Spring Security
- REST API
- PostgreSQL
- Docker / Docker Compose
- OpenAPI / Swagger
- Testes Unitários & Testes de Integração
- Arquitetura limpa (DTO, Camadas, Exception Handling)

---

## ▶️ Executando o Projeto

### Requisitos

#### Opção 1 - Executar com Docker (Recomendado)

- Docker
- Docker Compose

#### Opção 2 - Executar Localmente

- Java 21+
- Maven
- PostgreSQL

---

## 🐳 Docker

```bash
# clonar projeto 
git clone https://github.com/romulomotadev/portfolio-spring-01-user-service-api.git

# acessar diretório da aplicação
cd portfolio-spring-01-user-service-api

# constrói a imagem antes de subir
docker compose up --build

# Verificar containers
docker ps

# parar os containers
docker compose down

# subir novamente sem reconstruir
docker compose up

# subir em background
docker compose up -d
```
---

## 💻 Local

```bash
git clone https://github.com/romulomotadev/portfolio-spring-01-user-service-api.git

cd portfolio-spring-01-user-service-api

mvn clean install

mvn spring-boot:run
```

---

## 🧭 Visão Geral da Arquitetura

![Architecture](src/main/java/com/rpdevelopment/user_service_api/docs/diagrams/diagram-user-service.jpg)
---

## 📐 Arquitetura e Modelagem
O projeto foi desenvolvido com foco em escalabilidade e manutenção, utilizando padrões de mercado:

* **Arquitetura em Camadas:** Separação entre Controller, Service e Repository, promovendo baixo acoplamento e alta coesão.
* **Modelagem de Dados:** Relacionamentos complexos $N:N$ para Perfis (Roles) e $1:1$ para detalhes de Pessoa e Endereço.
* **Projeções (Spring Data JPA):** Otimização de consultas ao banco de dados, buscando apenas os campos necessários através de interfaces de projeção.
* **Handlers de Exceção:** Tratamento global de erros através de um `@ControllerAdvice`, garantindo respostas HTTP padronizadas e seguras.
* **DTOs (Data Transfer Objects):** Camada de transporte de dados para isolar a lógica de negócio das entidades de persistência.

---

## 🧪 Qualidade de Software
A aplicação possui uma sólida cobertura de testes automatizados, garantindo a confiabilidade das regras de negócio:

* **Testes de Integração (IT):** Validação de fluxos completos de ponta a ponta, desde o Controller até a persistência.
* **Testes de Unidade:** Foco em lógica isolada com o uso intensivo de Mocks.
* **Factories de Teste:** Implementação de fábricas de objetos (`UserFactory`) para garantir cenários de teste consistentes e reutilizáveis.

---

## 📂 Documentação e Testes Manuais
Para facilitar a exploração da API, o projeto inclui:
* **Diagramas:** Modelagem de Entidade e Classe detalhada (disponível em `src/main/java/com.rpdevelopment.user_service_api/docs/diagrams`).
* **Postman:** Collection e Environment prontos para importar e testar os endpoints imediatamente. (disponível em `src/main/java/com.rpdevelopment.user_service_api/docs/postman`).
* **Swagger:** Documentação interativa disponível via UI ao rodar a aplicação.

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI: http://localhost:8080/v3/api-docs

---

## 🔐 Autenticação (JWT)

Esta API utiliza autenticação baseada em **JWT (Bearer Token)** para proteger os endpoints.

### 📌 Como obter o token

1. Realize a autenticação no endpoint `/login`
2. Copie o token JWT retornado na resposta

### 📌 Como utilizar no Swagger

1. Clique no botão **Authorize** no Swagger UI
2. Insira o token no formato:

```
Bearer seu_token_aqui
```

3. Clique em **Authorize**
4. Agora você pode acessar os endpoints protegidos

### 🔒 Endpoints protegidos

Todos os endpoints exigem autenticação, exceto os responsáveis por login/autenticação.

### 📥 Exemplo de resposta do login

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 🚀 Endpoints
Alguns endpoints principais da API estão listados abaixo. Para a documentação completa, consulte o Swagger.

Para a documentação completa, acesse o Swagger:
👉 http://localhost:8080/swagger-ui.html

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `/users` | GET | Lista todos os usuários |
| `/users` | POST | Cria um novo usuário |
| `/users/{id}` | PUT | Atualiza um usuário |
| `/users/{id}` | DELETE | Remove um usuário |
| `/users/{userId}/roles/{roleId}` | PUT | Adiciona ou atualiza roles de um usuário |
| `/login` | POST | Gera token JWT |

### Exemplo de GET `/users`

```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "roles": ["ROLE_ADMIN"]
  }
]
```
---

## 🚀 Próximos Passos
Este projeto é a primeira etapa de um ecossistema de três microsserviços voltados para o portfólio profissional:
1. **User Service:** Gestão de Usuários e Autenticação.
2. **Product & Inventory Service:** Gestão de estoque e catálogo.
3. **Order Service:** Orquestração de pedidos com integração entre serviços e mensageria (RabbitMQ) - (Em desenvolvimento).

---

### 👨‍💻 Autor
**Rômulo Mota** - Desenvolvedor Java Backend em transição de carreira.
* [LinkedIn](https://linkedin.com/in/romulomotadev)
* [GitHub](https://github.com/romulomotadev)