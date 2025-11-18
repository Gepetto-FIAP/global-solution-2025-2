# Backend Java - Catálogo de Habilidades API

Backend REST API desenvolvido em **Jakarta EE** para o sistema Catálogo de Habilidades Integrado à Alura.

## Tecnologias

- **Java 17**
- **Jakarta EE** (JAX-RS)
- **Jersey** para implementação JAX-RS
- **Grizzly HTTP Server** (servidor embutido)
- **JDBC** com **HikariCP** (connection pool)
- **Oracle Database**
- **JWT** (JSON Web Tokens)
- **BCrypt** para hash de senhas
- **Jackson** para serialização JSON

## Estrutura do Projeto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/catalogo/habilidades/
│   │   │   ├── Main.java                    # Classe principal (inicia servidor Grizzly)
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java       # Endpoints REST de autenticação
│   │   │   │   └── GlobalExceptionHandler.java  # Tratamento global de exceções
│   │   │   ├── service/
│   │   │   │   └── AuthService.java          # Lógica de negócio de autenticação
│   │   │   ├── repository/
│   │   │   │   └── UsuarioRepository.java    # Acesso a dados (JDBC)
│   │   │   ├── model/
│   │   │   │   └── Usuario.java             # Modelo de dados
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   └── UserResponse.java
│   │   │   ├── security/
│   │   │   │   └── JwtUtil.java             # Utilitários JWT
│   │   │   └── config/
│   │   │       ├── JaxRsApplication.java    # Configuração JAX-RS
│   │   │       ├── CorsFilter.java           # Filtro CORS
│   │   │       └── PersistenceConfig.java   # Configuração JDBC/HikariCP
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── run.sh                                    # Script para executar a aplicação
```

## Requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **Oracle Database** 12c ou superior (ou acesso ao banco Oracle da FIAP)
- Acesso ao banco de dados configurado

## Configuração

### 1. Variáveis de Ambiente

As variáveis de ambiente são carregadas automaticamente do arquivo `.env.local` na raiz do projeto através do script `run.sh`.

**Arquivo `.env.local` (na raiz do projeto):**
```bash
# Oracle Database (usado pelo backend Java)
ORACLE_USER=seu_usuario
ORACLE_PASSWORD=sua_senha
ORACLE_CONNECT_STRING=oracle.fiap.com.br:1521/orcl

# JWT Secret (usado pelo backend Java para assinar tokens)
JWT_SECRET=sua-chave-secreta-super-forte-altere-em-producao
JWT_EXPIRATION=604800000  # opcional, padrão: 7 dias em milissegundos
```

**Nota:** O formato do `ORACLE_CONNECT_STRING` pode variar:
- `oracle.fiap.com.br:1521/orcl` (banco Oracle da FIAP)
- `localhost:1521/XE` (para Oracle XE local)
- `hostname:porta:SID`
- `hostname:porta/service_name`

### 2. Compilar o Projeto

```bash
cd backend
mvn clean package
```

Isso gerará um arquivo JAR executável em `target/habilidades-api.jar`

## Executando Localmente

### Opção 1: Usando o script run.sh (Recomendado)

O script `run.sh` carrega automaticamente as variáveis de ambiente do `.env.local`:

```bash
cd backend
chmod +x run.sh
./run.sh
```

### Opção 2: Executando o JAR diretamente

Se preferir executar diretamente, certifique-se de exportar as variáveis de ambiente primeiro:

```bash
cd backend

# Exportar variáveis de ambiente
export ORACLE_USER=seu_usuario
export ORACLE_PASSWORD=sua_senha
export ORACLE_CONNECT_STRING=oracle.fiap.com.br:1521/orcl
export JWT_SECRET=sua-chave-secreta

# Executar o JAR
java -jar target/habilidades-api.jar
```

### Opção 3: Executando via Maven

```bash
cd backend

# Exportar variáveis de ambiente primeiro
export ORACLE_USER=seu_usuario
export ORACLE_PASSWORD=sua_senha
export ORACLE_CONNECT_STRING=oracle.fiap.com.br:1521/orcl
export JWT_SECRET=sua-chave-secreta

# Executar via Maven
mvn exec:java -Dexec.mainClass="com.catalogo.habilidades.Main"
```

## Verificação

Após iniciar, a aplicação estará disponível em:
- **URL Base:** `http://localhost:8080`
- **API Base:** `http://localhost:8080/api`
- **Health Check:** `http://localhost:8080/api/auth/login` (deve retornar JSON, não HTML)

Você verá no console:
```
Iniciando Catálogo de Habilidades API...
Servidor disponível em: http://localhost:8080/
API disponível em: http://localhost:8080/api/
```

## Endpoints Disponíveis

### POST /api/auth/register
Registra um novo usuário.

**Exemplo de requisição:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -H "Origin: http://localhost:3000" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

**Resposta de sucesso:**
```json
{
  "success": true,
  "message": "Cadastro realizado com sucesso!",
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com"
  },
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### POST /api/auth/login
Realiza login do usuário.

**Exemplo de requisição:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -H "Origin: http://localhost:3000" \
  -d '{
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

**Resposta de sucesso:**
```json
{
  "success": true,
  "message": "Login realizado com sucesso!",
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com"
  },
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### GET /api/auth/me
Retorna dados do usuário autenticado.

**Exemplo de requisição:**
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Origin: http://localhost:3000"
```

### POST /api/auth/logout
Realiza logout (opcional, apenas retorna sucesso).

## CORS

A API está configurada para aceitar requisições do frontend em `http://localhost:3000`. Os headers CORS são adicionados automaticamente a todas as respostas:

- `Access-Control-Allow-Origin: http://localhost:3000`
- `Access-Control-Allow-Credentials: true`
- `Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, HEAD`
- `Access-Control-Allow-Headers: origin, content-type, accept, authorization`

## Troubleshooting

### Erro de Conexão com Oracle

1. Verifique se o Oracle está acessível
2. Confirme as credenciais no arquivo `.env.local`
3. Verifique se o formato do `ORACLE_CONNECT_STRING` está correto
4. Teste a conexão manualmente:
   ```bash
   sqlplus usuario/senha@oracle.fiap.com.br:1521/orcl
   ```

### Erro de Porta em Uso

Se a porta 8080 estiver em uso, você pode alterar no arquivo `Main.java`:

```java
public static final String BASE_URI = "http://localhost:8081/";  // Alterar porta
```

### Erro de Compilação

Certifique-se de que:
- Java 17 está instalado (`java -version`)
- Maven está instalado (`mvn -version`)
- Todas as dependências foram baixadas (`mvn clean install`)

### Variáveis de Ambiente não Carregadas

Se o script `run.sh` não estiver carregando as variáveis:
1. Verifique se o arquivo `.env.local` existe na raiz do projeto
2. Verifique se o caminho relativo está correto (`../env.local` do diretório `backend`)
3. Execute manualmente exportando as variáveis antes de rodar o JAR

### Erro "MessageBodyWriter not found"

Se você receber erro sobre `MessageBodyWriter not found for media type=application/json`, certifique-se de que o `JacksonFeature` está registrado no `JaxRsApplication.java`.

## Arquitetura

### Diferenças do Spring Boot

Esta aplicação foi migrada de Spring Boot para Jakarta EE com arquitetura simplificada:

1. **Empacotamento**: JAR executável único (não WAR)
2. **Servidor**: Grizzly HTTP Server embutido (não requer servidor de aplicação externo)
3. **Injeção de Dependências**: Instâncias diretas (não usa CDI)
4. **REST**: JAX-RS (Jersey) com `@Path`, `@GET`, `@POST`
5. **Persistência**: JDBC direto com HikariCP (não usa JPA/Hibernate)
6. **Configuração**: Variáveis de ambiente + código Java

### Componentes Principais

- **Main.java**: Inicia o servidor Grizzly e configura a aplicação JAX-RS
- **JaxRsApplication**: Registra recursos REST, filtros e providers
- **PersistenceConfig**: Gerencia pool de conexões HikariCP
- **CorsFilter**: Adiciona headers CORS a todas as respostas
- **GlobalExceptionHandler**: Trata exceções e retorna JSON padronizado

## Próximos Passos

1. ✅ API funcionando com Grizzly
2. ✅ CORS configurado
3. ✅ Variáveis de ambiente carregadas do `.env.local`
4. ✅ Banco de dados conectado
5. 🔄 Integrar com o frontend Next.js
6. 🔄 Testar autenticação completa (registro → login → acesso protegido)
7. 🔄 Adicionar mais endpoints conforme necessário

## Desenvolvimento

Para desenvolvimento, você pode usar o Maven em modo watch:

```bash
# Terminal 1: Rodar a aplicação
cd backend
./run.sh

# Terminal 2: Fazer alterações e recompilar
cd backend
mvn clean package
# Reiniciar a aplicação (Ctrl+C e rodar ./run.sh novamente)
```

## Licença

Este projeto faz parte do trabalho acadêmico da FIAP.
