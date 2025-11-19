# Instruções para Executar o Backend Java

## Pré-requisitos

1. **Java 17** instalado
2. **Maven 3.6+** instalado
3. **Oracle Database** configurado e rodando
4. Tabelas criadas usando o script `docs/script_banco_dados.sql`

## Configuração

### 1. Variáveis de Ambiente

Configure as seguintes variáveis de ambiente antes de executar:

```bash
export ORACLE_USER=seu_usuario
export ORACLE_PASSWORD=sua_senha
export ORACLE_CONNECT_STRING=localhost:1521:XE
export JWT_SECRET=sua-chave-secreta-super-forte-altere-em-producao
```

**Nota:** O formato do `ORACLE_CONNECT_STRING` pode variar:
- `localhost:1521:XE` (para Oracle XE)
- `hostname:porta:SID`
- `hostname:porta/service_name`

### 2. Compilar o Projeto

```bash
cd backend
mvn clean install
```

### 3. Executar a Aplicação

```bash
mvn spring-boot:run
```

Ou execute diretamente a classe `HabilidadesApplication.java` na sua IDE.

## Verificação

Após iniciar, a aplicação estará disponível em:
- **URL Base:** `http://localhost:8080`
- **Health Check:** `http://localhost:8080/api/auth/login` (deve retornar erro 400, não 404)

## Endpoints Disponíveis

### POST /api/auth/register
Registra um novo usuário.

**Exemplo de requisição:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

### POST /api/auth/login
Realiza login do usuário.

**Exemplo de requisição:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

### GET /api/auth/me
Retorna dados do usuário autenticado.

**Exemplo de requisição:**
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### POST /api/auth/logout
Realiza logout (opcional).

## Troubleshooting

### Erro de Conexão com Oracle

1. Verifique se o Oracle está rodando
2. Confirme as credenciais (usuário, senha, connect string)
3. Verifique se o driver Oracle está no classpath (deve estar no pom.xml)

### Erro de Porta em Uso

Se a porta 8080 estiver em uso, altere em `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Erro de Compilação

Certifique-se de que:
- Java 17 está instalado (`java -version`)
- Maven está instalado (`mvn -version`)
- Todas as dependências foram baixadas (`mvn clean install`)

## Logs

Para ver logs detalhados, execute com o profile `dev`:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Ou configure no `application.properties`:
```properties
spring.profiles.active=dev
```

## Próximos Passos

1. Testar os endpoints usando Postman ou curl
2. Integrar com o frontend Next.js
3. Verificar se o CORS está funcionando corretamente
4. Testar autenticação completa (registro → login → acesso protegido)

