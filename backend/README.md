# Backend Java - Catálogo de Habilidades API

Backend REST API desenvolvido em Spring Boot para o sistema Catálogo de Habilidades Integrado à Alura.

## Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Oracle Database**
- **JWT** (JSON Web Tokens)
- **BCrypt** para hash de senhas

## Estrutura do Projeto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/catalogo/habilidades/
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java
│   │   │   ├── service/
│   │   │   │   └── AuthService.java
│   │   │   ├── repository/
│   │   │   │   └── UsuarioRepository.java
│   │   │   ├── model/
│   │   │   │   └── Usuario.java
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   └── UserResponse.java
│   │   │   ├── security/
│   │   │   │   ├── JwtUtil.java
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   └── HabilidadesApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
└── pom.xml
```

## Requisitos

- Java 17 ou superior
- Maven 3.6+
- Oracle Database 12c ou superior
- Acesso ao banco de dados configurado

## Configuração

### 1. Variáveis de Ambiente

Configure as seguintes variáveis de ambiente:

```bash
export ORACLE_USER=seu_usuario
export ORACLE_PASSWORD=sua_senha
export ORACLE_CONNECT_STRING=localhost:1521:XE
export JWT_SECRET=sua-chave-secreta-super-forte-altere-em-producao
```

Ou crie um arquivo `.env` na raiz do projeto backend (não será commitado).

### 2. Configuração do Banco de Dados

Certifique-se de que o banco de dados Oracle está configurado e que as tabelas foram criadas usando o script em `docs/script_banco_dados.sql`.

### 3. Compilar e Executar

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

Ou execute diretamente a classe `HabilidadesApplication.java` na sua IDE.

## Endpoints da API

### POST /api/auth/register
Registra um novo usuário.

### POST /api/auth/login
Realiza login do usuário.

### POST /api/auth/logout
Realiza logout (opcional).

### GET /api/auth/me
Retorna dados do usuário autenticado atual.

Para mais detalhes, consulte `docs/API_CONTRACTS.md`.

## Porta Padrão

A aplicação roda na porta **8080** por padrão.

## CORS

A API está configurada para aceitar requisições de `http://localhost:3000` (frontend Next.js).

## Segurança

- Senhas são hasheadas com BCrypt (10 rounds)
- Tokens JWT com expiração de 7 dias
- Validação de dados de entrada
- Filtro de autenticação JWT

## Desenvolvimento

Para ativar logs detalhados, use o profile `dev`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

