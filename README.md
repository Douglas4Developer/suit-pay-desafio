```markdown
# SuitPay – Credit Limit API

API desenvolvida como parte de teste técnico, com foco em **boas práticas**, **segurança** e **qualidade de código**.  
O objetivo é gerenciar limites de crédito de clientes, com auditoria e regras de negócio específicas para clientes VIP.

---

## 🚀 Tecnologias e Padrões Utilizados

- **Java 17**  
- **Spring Boot 3** (Web, Data JPA, Security, Validation, Retry)  
- **Gradle** para build  
- **SQL Server** como banco de dados  
- **Hibernate/JPA** com otimistic locking  
- **JWT + Spring Security** para autenticação/autorização  
- **Lombok** para reduzir boilerplate  
- **Bean Validation** para validações  
- **@ControllerAdvice** para tratamento centralizado de erros  

---





## 📂 Estrutura do Projeto

```

src/main/java/com/douglas/suitpay
├──> controller # Controllers REST
├──> domain # Entidades (Customer, CreditLimitHistory, User, Role)
├──> dto # DTOs (requests/responses)
├──> exception # Exceções e handlers
├──> repository # Repositórios JPA
├──> security # Configuração JWT + Security
├──> service # Regras de negócio (interfaces + impl)
└──> BootSeed.java # Seed inicial (roles + admin)

Evidências dos testes funcionais: 
<img width="1689" height="933" alt="image" src="https://github.com/user-attachments/assets/0982795f-2c18-454a-81ed-2a30e79ab538" />
<img width="608" height="337" alt="image" src="https://github.com/user-attachments/assets/7bdfd340-1c1f-4635-b625-53687292f2fa" />
<img width="1764" height="543" alt="image" src="https://github.com/user-attachments/assets/17088fc5-0a43-4ae2-a28b-82792017d748" />
<img width="1476" height="496" alt="image" src="https://github.com/user-attachments/assets/a83e3022-9fbf-4bc1-b919-f755efab9b47" />
# Sem a ROLE CREDIT_LIMIT_ADMIN
<img width="1475" height="521" alt="image" src="https://github.com/user-attachments/assets/ef486c4a-e31b-456e-9bb2-348bc6978c55" />
# Com a ROLE CREDIT_LIMIT_ADMIN
<img width="1483" height="578" alt="image" src="https://github.com/user-attachments/assets/b36d0e79-4ee7-4736-b447-726f24ec20df" />
````

---

## 📑 Regras de Negócio

- O **limite de crédito** não pode ser negativo.  
- Apenas usuários com role **`ROLE_CREDIT_LIMIT_ADMIN`** podem alterar limites.  
- Se o cliente for **VIP**, o limite precisa ser maior ou igual a um valor mínimo configurável (`credit.vip.min`).  
- Toda alteração gera histórico em `CreditLimitHistory` (quem alterou, quando, valor antigo e novo).  
- Concorrência garantida via **optimistic locking** com `@Version` e política de retry.

---

## 🔐 Segurança

- Autenticação via **JWT** (`/auth/login` e `/auth/refresh`).  
- Endpoint `/auth/signup` para criar usuários comuns.  
- Endpoint `/admin/users` para criação de usuários com roles arbitrárias (apenas admins).  
- Tokens com tempo de expiração configurável (`jwt.access.ttl` e `jwt.refresh.ttl`).  
- Apenas usuários autenticados podem acessar a API, com regras específicas por endpoint.

---

## 🗃️ Banco de Dados

### Tabelas principais

- **customer**  
  - `id`, `is_vip`, `credit_limit`, `version`  

- **credit_limit_history**  
  - `id`, `customer_id`, `old_limit`, `new_limit`, `changed_by`, `changed_at`  

- **users** / **roles** / **users_roles**  
  - Autenticação/autorização  

### Seed inicial

Na primeira execução, o projeto cria automaticamente:  
- Roles: `ROLE_USER` e `ROLE_CREDIT_LIMIT_ADMIN`  
- Usuário admin: `admin / 123456`  

---

## ⚙️ Configuração

Arquivo `application.properties`:

```properties
spring.application.name=suitpay
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=suitpay;encrypt=true;trustServerCertificate=true
spring.datasource.username=suitpay_user
spring.datasource.password=teste123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.open-in-view=false

# JWT
security.jwt.secret=uma_chave_secreta_segura_de_32_chars_no_minimo
security.jwt.access.ttl=PT15M
security.jwt.refresh.ttl=PT7D

# Negócio
credit.vip.min=1000.00
````

---

## ▶️ Como Rodar

1. Suba um SQL Server local (pode usar Docker):

   Crie o database e usuário `suitpay_user`.

2. Clone o projeto e rode:

   ```bash
   ./gradlew bootRun
   ```

3. A API sobe em `http://localhost:8080`.
 
## 🌐 Endpoints Principais

### Auth

* `POST /auth/signup` → cria usuário comum
* `POST /auth/login` → autentica e retorna `accessToken` e `refreshToken`
* `POST /auth/refresh` → gera novo `accessToken`
* `GET /auth/me` → retorna usuário logado

### Admin

* `POST /admin/users` → cria usuário com roles (apenas admin)

### Customers

* `POST /customers` → cria cliente
* `GET /customers/{id}` → busca cliente
* `GET /customers?page=&size=` → lista paginada

### Credit Limit

* `GET /customers/{id}/credit-limit` → consulta limite atual
* `PUT /customers/{id}/credit-limit` → altera limite (ADMIN apenas)
* `GET /customers/{id}/credit-limit/history?page=&size=` → histórico paginado

---

## 📬 Collection Postman

Uma collection completa (`SuitPay_API.postman_collection.json`) acompanha o repositório.
Ela inclui:

* Auth (signup, login, refresh, me)
* Admin (criar usuários com roles)
* Customers
* Credit Limit (consultar, atualizar, histórico)
* Tests automáticos para atualizar o token JWT ao logar.

---

## ✅ Critérios de Aceite (Checklist)

* [x] Consulta de limite atual
* [x] Atualização com regras + auditoria
* [x] Paginação do histórico
* [x] Autorização aplicada
* [x] Transações + locking otimista
* [x] Limite VIP configurável
* [x] README completo
* [x] Collection Postman disponível
---

## 👤 Autor

**Douglas Soares de Souza Ferreira**
[GitHub](https://github.com/Douglas4Developer)
https://www.linkedin.com/in/douglas-soares-de-souza-ferreira/

