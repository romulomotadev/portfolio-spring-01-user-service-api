# 👤 User Service API

Este microsserviço é o pilar de autenticação e gestão de perfis para uma arquitetura baseada em microsserviços. Ele gerencia desde o cadastro de usuários (Pessoa Física e Jurídica) até a autorização de acesso via tokens seguros, utilizando o **Spring Authorization Server**.

---

## 📐 Arquitetura e Modelagem
O projeto foi desenvolvido com foco em escalabilidade e manutenção, utilizando padrões de mercado:

* **Modelagem de Dados:** Relacionamentos complexos $N:N$ para Perfis (Roles) e $1:1$ para detalhes de Pessoa e Endereço.
* **Projeções (Spring Data JPA):** Otimização de consultas ao banco de dados, buscando apenas os campos necessários através de interfaces de projeção.
* **Handlers de Exceção:** Tratamento global de erros através de um `@ControllerAdvice`, garantindo respostas HTTP padronizadas e seguras.
* **DTOs (Data Transfer Objects):** Camada de transporte de dados para isolar a lógica de negócio das entidades de persistência.

---

## 🛠️ Tecnologias e Ferramentas
* **Linguagem:** Java (8+) 
* **Framework:** Spring Boot 3, Spring Data JPA, Spring Security 
* **Segurança:** Autenticação e autorização com JWT (OAuth2) 
* **Banco de Dados:** PostgreSQL (Produção/Dev) e H2 (Testes) 
* **APIs:** REST, Swagger/OpenAPI 
* **Testes:** JUnit 5, Mockito 
* **Build:** Maven 

---

## 🧪 Qualidade de Software (TDD)
A aplicação possui uma sólida cobertura de testes automatizados, garantindo a confiabilidade das regras de negócio:

* **Testes de Integração (IT):** Validação de fluxos completos de ponta a ponta, desde o Controller até a persistência.
* **Testes de Unidade:** Foco em lógica isolada com o uso intensivo de Mocks.
* **Factories de Teste:** Implementação de fábricas de objetos (`UserFactory`) para garantir cenários de teste consistentes e reutilizáveis.

---

## 📂 Documentação e Testes Manuais
Para facilitar a exploração da API, o projeto inclui:
* **Diagramas:** Modelagem de Entidade e Classe detalhada (disponível em `src/main/resources/docs/diagrams`).
* **Postman:** Collection e Environment prontos para importar e testar os endpoints imediatamente.
* **Swagger:** Documentação interativa disponível via UI ao rodar a aplicação.

---

### 🔹 JWT e Autenticação

Todos os endpoints requerem autenticação JWT:

1. Obtenha o token via endpoint `/login`.
2. Clique em **Authorize** no Swagger UI.
3. Cole o token no formato `Bearer <token>`.
4. Execute as requisições nos endpoints protegidos.

---

## 🚀 Endpoints Principais

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
1. **Product & Inventory Service:** Gestão de estoque e catálogo (Em desenvolvimento).
2. **Order Service:** Orquestração de pedidos com integração entre serviços e mensageria (RabbitMQ).
3. **Infraestrutura:** Conteinerização com **Docker**, monitoramento com **Prometheus/Grafana** e deploy via **AWS**.

---

### 👨‍💻 Autor
**Rômulo Mota** - Desenvolvedor Java Backend em transição de carreira.
* [LinkedIn](https://linkedin.com/in/romulomotadev)
* [GitHub](https://github.com/romulomotadev)