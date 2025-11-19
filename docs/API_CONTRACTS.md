# Contratos de API - Backend Java

Este documento especifica os endpoints REST que o backend Java deve implementar para comunicação com o frontend Next.js.

## Base URL

```
http://localhost:8080
```

A URL base pode ser configurada via variável de ambiente `NEXT_PUBLIC_API_URL` no frontend.

---

## Autenticação

Todas as requisições protegidas devem incluir o token JWT no header:

```
Authorization: Bearer {token}
```

O token será enviado pelo frontend após login/registro bem-sucedido.

---

## Endpoints

### 1. POST /api/auth/register

Registra um novo usuário no sistema.

**Request Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```

**Response Success (201 Created):**
```json
{
  "success": true,
  "message": "Cadastro realizado com sucesso!",
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response Error (400 Bad Request):**
```json
{
  "success": false,
  "message": "Email já cadastrado"
}
```

**Validações:**
- Email deve ser único
- Senha deve ter no mínimo 6 caracteres
- Nome é obrigatório
- Email deve ter formato válido

**Notas:**
- A senha deve ser hasheada com bcrypt antes de ser armazenada no banco
- O token JWT deve conter: userId, email, nome
- Expiração do token: 7 dias

---

### 2. POST /api/auth/login

Realiza login do usuário.

**Request Body:**
```json
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

**Response Success (200 OK):**
```json
{
  "success": true,
  "message": "Login realizado com sucesso!",
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response Error (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Email ou senha incorretos"
}
```

**Validações:**
- Verificar se email existe no banco
- Comparar senha fornecida com hash armazenado usando bcrypt
- Retornar erro genérico para não expor se email existe ou não

---

### 3. POST /api/auth/logout

Realiza logout do usuário (opcional - pode ser apenas no frontend).

**Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200 OK):**
```json
{
  "success": true,
  "message": "Logout realizado com sucesso"
}
```

**Notas:**
- Este endpoint é opcional, pois o logout pode ser feito apenas removendo o token no frontend
- Se implementado, pode invalidar o token no backend (blacklist)

---

### 4. GET /api/auth/me

Retorna dados do usuário autenticado atual.

**Headers:**
```
Authorization: Bearer {token}
```

**Response Success (200 OK):**
```json
{
  "success": true,
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com"
  }
}
```

**Response Error (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Token inválido ou expirado"
}
```

**Validações:**
- Verificar se token é válido
- Verificar se token não expirou
- Buscar usuário no banco pelo ID do token

---

## Estrutura do Token JWT

O token JWT deve conter o seguinte payload:

```json
{
  "userId": 1,
  "email": "joao@email.com",
  "nome": "João Silva",
  "iat": 1234567890,
  "exp": 1234567890
}
```

- `userId`: ID do usuário no banco de dados
- `email`: Email do usuário
- `nome`: Nome do usuário
- `iat`: Timestamp de criação
- `exp`: Timestamp de expiração (7 dias após criação)

---

## Códigos de Status HTTP

- `200 OK`: Requisição bem-sucedida
- `201 Created`: Recurso criado com sucesso (registro)
- `400 Bad Request`: Dados inválidos ou erro de validação
- `401 Unauthorized`: Não autenticado ou token inválido
- `500 Internal Server Error`: Erro interno do servidor

---

## Formato de Erros

Todas as respostas de erro devem seguir o formato:

```json
{
  "success": false,
  "message": "Mensagem de erro descritiva"
}
```

---

## Banco de Dados

### Tabela USUARIO

```sql
CREATE TABLE USUARIO (
    id_usuario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);
```

### Hash de Senha

- Use bcrypt com salt rounds = 10
- Armazene apenas o hash, nunca a senha em texto plano
- Ao comparar senhas, use `bcrypt.compare(senhaFornecida, hashArmazenado)`

---

## Exemplo de Implementação Java

### Dependências Maven

```xml
<!-- Jakarta REST (JAX-RS) -->
<dependency>
    <groupId>jakarta.ws.rs</groupId>
    <artifactId>jakarta.ws.rs-api</artifactId>
    <version>3.1.0</version>
</dependency>

<!-- Jersey com Grizzly (Servidor embutido) -->
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-grizzly2-http</artifactId>
    <version>3.1.3</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-json-jackson</artifactId>
    <version>3.1.3</version>
</dependency>

<!-- Oracle Database -->
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>23.3.0.23.09</version>
</dependency>

<!-- HikariCP (Connection Pool) -->
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>

<!-- BCrypt -->
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
```

### Estrutura de Classes Sugerida

```
com.catalogo.habilidades
├── controller
│   └── AuthController.java
├── service
│   └── AuthService.java
├── repository
│   └── UsuarioRepository.java
├── model
│   └── Usuario.java
├── dto
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── AuthResponse.java
└── security
    └── JwtUtil.java
```

---

## Notas Finais

- Todas as senhas devem ser hasheadas antes de serem armazenadas
- Tokens JWT devem ser assinados com uma chave secreta forte
- Valide todos os dados de entrada
- Retorne mensagens de erro descritivas mas seguras
- Use HTTPS em produção
- Implemente rate limiting para prevenir ataques de força bruta

