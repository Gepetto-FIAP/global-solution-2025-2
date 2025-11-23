# Catálogo de Habilidades Integrado à Alura
## Global Solution 2025 - FIAP

### 📋 Descrição do Projeto

Sistema desenvolvido para cadastrar habilidades e sugerir cursos da Alura relacionados, incentivando o aprendizado contínuo e a requalificação profissional. O sistema está alinhado ao tema **"O Futuro do Trabalho"**.

### 🎯 Objetivo

Criar um sistema simples onde o usuário possa:
- ✅ **Cadastrar habilidades** (nome, categoria, descrição)
- ✅ **Listar habilidades** cadastradas
- ✅ **Editar/Excluir** habilidades
- ✅ **Ver cursos sugeridos** - Ao clicar numa habilidade, o sistema mostra cursos simulados da Alura relacionados pela categoria
- ✅ **Filtrar por categoria** (opcional)
- ✅ **Autenticação de usuários** (login e registro)

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Node.js** 18+ e npm
- **Java** 21 LTS ou superior
- **Maven** 3.9.9+
- **Oracle Database** 12c ou superior (com acesso configurado)
- **Git**
- **PowerShell** 5.1+ (Windows) ou **Bash** (Linux/Mac)

### 1️⃣ Configuração do Banco de Dados

#### 1.1. Criar o Banco de Dados

Execute o script SQL fornecido no Oracle:

```bash
sqlplus usuario/senha@database @docs/script_banco_dados.sql
```

Ou execute diretamente no SQL*Plus ou SQL Developer:

```sql
-- Conecte-se ao Oracle e execute:
@docs/script_banco_dados.sql
```

**Nota:** Certifique-se de ter privilégios adequados (CREATE TABLE, CREATE INDEX) no schema Oracle.

#### 1.2. Verificar as Tabelas Criadas

```sql
-- Listar tabelas do schema atual
SELECT table_name FROM user_tables ORDER BY table_name;

-- Deve retornar: CURSO, HABILIDADE, USUARIO
```

### 2️⃣ Configuração das Variáveis de Ambiente

#### 2.1. Frontend (Next.js)

Crie um arquivo `.env.local` na **raiz do projeto**:

```env
# URL base do backend Java REST API
NEXT_PUBLIC_API_URL=http://localhost:8080
```

#### 2.2. Backend (Java)

O backend precisa das seguintes variáveis de ambiente:

**Criar arquivo `.env.local` na raiz do projeto:**

```env
# Frontend - URL base do backend Java REST API
NEXT_PUBLIC_API_URL=http://localhost:8080

# Backend - Oracle Database
ORACLE_USER=seu_user
ORACLE_PASSWORD=sua_senha
ORACLE_CONNECT_STRING=oracle.fiap.com.br:1521/orcl

# Backend - JWT Secret (chave secreta para assinar tokens JWT)
JWT_SECRET=sua-chave-secreta-super-forte-altere-em-producao
JWT_EXPIRATION=604800000
```

**Formato da Connection String:**
- Para service name (com `/`): `host:port/service_name`
- Para SID (com `:`): `host:port:sid`

**Exemplo FIAP:**
```env
ORACLE_USER=seu_user
ORACLE_PASSWORD=sua_senha
ORACLE_CONNECT_STRING=oracle.fiap.com.br:1521/orcl
```

📖 Para mais detalhes, consulte `docs/env_variables.md`

### 3️⃣ Executar o Backend (Java)

#### 3.1. Navegar para o diretório do backend

```bash
cd backend
```

#### 3.2. Executar usando PowerShell (Windows) - Recomendado ✅

O script `run.ps1` carrega automaticamente as variáveis do `.env.local`:

```powershell
.\run.ps1
```

**Se houver erro de ExecutionPolicy:**

```powershell
# Permitir execução apenas para esta sessão
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process

# Executar novamente
.\run.ps1
```

**O que o script faz:**
1. 📂 Carrega automaticamente as variáveis do arquivo `.env.local` da raiz do projeto
2. ✅ Verifica se o JAR já foi compilado
3. 🔨 Se não existir, compila com `mvn clean package`
4. 🚀 Executa o JAR com as variáveis de ambiente configuradas
5. ⚙️ Configura a porta 8080 (ou variável PORT se definida)

#### 3.3. Executar usando Bash (Linux/Mac)

```bash
# Dar permissão de execução (primeira vez)
chmod +x run.sh

# Executar
./run.sh
```

#### 3.4. Executar manualmente (alternativa)

Se preferir executar manualmente sem os scripts:

**Windows (PowerShell):**
```powershell
# Carregar variáveis do .env.local
Get-Content ..\.env.local | ForEach-Object {
    if ($_ -match '^([^=]+)=(.+)$') {
        [Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
    }
}

# Compilar (se necessário)
mvn clean package -DskipTests

# Executar
java -jar target/habilidades-api.jar
```

**Linux/Mac (Bash):**
```bash
# Carregar variáveis do .env.local
export $(grep -v '^#' ../.env.local | grep -v '^$' | xargs)

# Compilar (se necessário)
mvn clean package -DskipTests

# Executar
java -jar target/habilidades-api.jar
```

#### 3.5. Verificar se o Backend está rodando

O backend deve iniciar na porta **8080**. Você verá uma mensagem similar a:

```
🚀 Servidor HTTP iniciado em: http://0.0.0.0:8080/
Pressione Ctrl+C para parar o servidor
```

Teste o endpoint de saúde:

```bash
curl http://localhost:8080/api/auth/login
```

Se retornar um erro de validação (esperado sem body), significa que o servidor está funcionando! ✅

### 4️⃣ Executar o Frontend (Next.js)

#### 4.1. Abrir um novo terminal

Mantenha o backend rodando e abra um novo terminal.

#### 4.2. Instalar dependências (primeira vez)

```bash
# Na raiz do projeto
npm install
```

#### 4.3. Executar o servidor de desenvolvimento

```bash
npm run dev
```

#### 4.4. Acessar a aplicação

Abra seu navegador em:

```
http://localhost:3000
```

### 5️⃣ Testar a Aplicação

#### 5.1. Criar uma conta

1. Acesse `http://localhost:3000/auth/register`
2. Preencha o formulário:
   - Nome: Seu nome
   - Email: seu@email.com
   - Senha: senha123
   - Confirmar Senha: senha123
3. Clique em "Criar Conta"

#### 5.2. Fazer Login

1. Acesse `http://localhost:3000/auth/login`
2. Use as credenciais criadas
3. Você será redirecionado para o dashboard

#### 5.3. Testar via API (opcional)

```bash
# Registrar usuário
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste",
    "email": "teste@teste.com",
    "password": "senha123"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@teste.com",
    "password": "senha123"
  }'

# Obter dados do usuário (substitua TOKEN pelo token retornado)
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer TOKEN"
```

---

## 📊 Diagrama de Entidade-Relacionamento (DER)

### Entidades

O modelo de dados foi projetado com **3 entidades principais**, atendendo o requisito mínimo de **3 entidades com relacionamento**:

1. **USUARIO** - Gerencia usuários do sistema (autenticação)
2. **HABILIDADE** - Gerencia habilidades cadastradas (ex: Java, Comunicação, UX Design)
3. **CURSO** - Armazena cursos simulados da Alura relacionados às habilidades

### Relacionamentos

- ✅ **USUARIO → HABILIDADE** (1:N) - Usuário cadastra habilidades
- ✅ **HABILIDADE ↔ CURSO** (N:M) - Relacionamento por categoria
  - Uma habilidade pode estar relacionada a vários cursos da mesma categoria
  - Um curso pode estar relacionado a várias habilidades da mesma categoria
  - Relacionamento baseado na correspondência do campo `categoria`
  - Consulta SQL: `WHERE habilidade.categoria = curso.categoria`

📖 Para mais detalhes, consulte `docs/diagrama_entidade_relacionamento.md`

---

## 📁 Estrutura do Projeto

```
globalsolution2/
├── app/                          # Frontend Next.js
│   ├── (site)/                   # Rotas públicas (login, registro)
│   ├── app/                      # Dashboard (protegido)
│   ├── components/               # Componentes React
│   └── styles/                   # Estilos globais
├── backend/                      # Backend Java Spring Boot
│   ├── src/main/java/            # Código fonte Java
│   ├── pom.xml                   # Configuração Maven
│   └── run.sh                    # Script de execução
├── docs/                         # Documentação
│   ├── API_CONTRACTS.md          # Contratos da API REST
│   ├── ENV_VARIABLES.md          # Variáveis de ambiente
│   ├── diagrama_entidade_relacionamento.md
│   ├── diagrama_visual.txt
│   └── script_banco_dados.sql    # Script SQL
├── lib/                          # Bibliotecas e utilitários
├── .env.local                    # Variáveis de ambiente (não versionado)
├── .gitignore                    # Arquivos ignorados pelo Git
├── package.json                  # Dependências Node.js
└── README.md                     # Este arquivo
```

---

## 🗄️ Banco de Dados

### Tecnologia
- **SGBD:** Oracle Database
- **Versão:** Oracle 12c ou superior (para suporte a IDENTITY)
- **Charset:** UTF-8 (padrão Oracle)

### Características do Modelo

- ✅ **Normalização:** Modelo em 3NF (Terceira Forma Normal)
- ✅ **Relacionamento por Categoria:** Baseado na correspondência de valores
- ✅ **Índices:** Otimização para consultas por categoria
- ✅ **Flexibilidade:** Categorias como VARCHAR para permitir expansão
- ✅ **Segurança:** Senhas hasheadas com BCrypt

---

## 🛠️ Stack Tecnológica

### Frontend
- **Next.js** 16.0.3 (React 19.2.0)
- **TypeScript** 5+
- **CSS Modules**
- **React Router**

### Backend
- **Java** 21 LTS
- **Jersey** 3.1.3 (JAX-RS 3.1.0)
- **Grizzly HTTP Server** 4.0.0 (embedded)
- **Maven** 3.9.9
- **HikariCP** 5.0.1 (connection pooling)

### Banco de Dados
- **Oracle Database** 12c+ (FIAP: oracle.fiap.com.br:1521/orcl)

### Autenticação & Segurança
- **JWT** (jjwt 0.12.3) - JSON Web Tokens
- **BCrypt** (jbcrypt 0.4) - Hash de senhas

### Deployment
- **Vercel** (Frontend - Next.js)
- **Azure App Service** (Backend - Java JAR)

---

## 📚 Documentação Adicional

- **API Contracts:** `docs/API_CONTRACTS.md` - Documentação completa dos endpoints REST
- **Variáveis de Ambiente:** `docs/ENV_VARIABLES.md` - Guia de configuração
- **Diagrama ER:** `docs/diagrama_entidade_relacionamento.md` - Modelo de dados detalhado
- **Backend README:** `backend/README.md` - Documentação específica do backend
- **Instruções Backend:** `backend/INSTRUCOES.md` - Guia passo a passo do backend

---

## 🔧 Troubleshooting

### Backend não conecta ao Oracle

1. Verifique se as variáveis de ambiente estão carregadas:
   
   **Windows (PowerShell):**
   ```powershell
   $env:ORACLE_USER
   $env:ORACLE_CONNECT_STRING
   ```
   
   **Linux/Mac (Bash):**
   ```bash
   echo $ORACLE_USER
   echo $ORACLE_CONNECT_STRING
   ```

2. Verifique o formato da string de conexão:
   - Service name: `host:port/service_name`
   - SID: `host:port:sid`

3. Teste a conexão manualmente:
   ```bash
   sqlplus $ORACLE_USER/$ORACLE_PASSWORD@$ORACLE_CONNECT_STRING
   ```

### Erro ao executar run.ps1 no Windows

**Erro:** "run.ps1 cannot be loaded because running scripts is disabled on this system"

**Solução:**
```powershell
# Permitir execução de scripts apenas para esta sessão
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process

# Executar novamente
.\run.ps1
```

**Solução permanente (não recomendada):**
```powershell
# Execute como Administrador
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Backend não compila (erro Maven)

1. Verifique a versão do Java:
   ```bash
   java -version  # Deve ser 21 ou superior
   ```

2. Verifique a versão do Maven:
   ```bash
   mvn -version  # Deve ser 3.9+
   ```

3. Limpe e recompile:
   ```bash
   cd backend
   mvn clean install
   ```

### Frontend não conecta ao Backend

1. Verifique se o backend está rodando na porta 8080:
   ```bash
   curl http://localhost:8080/api/auth/login
   ```

2. Verifique a variável `NEXT_PUBLIC_API_URL` no `.env.local`:
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8080
   ```

3. Verifique o console do navegador (F12) para erros de CORS

### Pool de conexões Oracle fechado (ORA-17008)

Se você ver o erro "Closed Connection", reinicie o backend:

**Windows:**
```powershell
# Pare com Ctrl+C e execute novamente
.\run.ps1
```

**Linux/Mac:**
```bash
./run.sh
```

---

## 📝 Requisitos Atendidos

### Requisitos Técnicos
- ✅ **Mínimo 3 entidades:** USUARIO, HABILIDADE, CURSO
- ✅ **Relacionamentos:** 
  - Relacionamento 1:N (USUARIO → HABILIDADE)
  - Relacionamento N:M por categoria (HABILIDADE ↔ CURSO)
- ✅ **Chaves Primárias:** Todas as entidades possuem PK
- ✅ **Normalização:** Modelo normalizado
- ✅ **Backend Java REST API:** Implementado com Spring Boot
- ✅ **Frontend Next.js:** Implementado com React e TypeScript
- ✅ **Autenticação:** JWT e BCrypt

### Entregas do Projeto
- ✅ **Banco de Dados:** Script SQL completo (`docs/script_banco_dados.sql`)
- ✅ **Diagrama ER:** Documentação completa (`docs/diagrama_entidade_relacionamento.md`)
- ✅ **Backend Java:** API REST completa (`backend/`)
- ✅ **Frontend Next.js:** Interface completa (`app/`)
- ✅ **Documentação:** README com instruções de uso

---

## 💡 Funcionalidades do Sistema

### Autenticação
- ✅ Registro de usuários
- ✅ Login com JWT
- ✅ Proteção de rotas
- ✅ Hash de senhas com BCrypt

### CRUD Completo
- ✅ Cadastrar habilidade
- ✅ Listar habilidades
- ✅ Editar habilidade
- ✅ Excluir habilidade
- ✅ Filtrar por categoria

### Integração Simulada com Alura
- ✅ Ao selecionar uma habilidade, o sistema mostra cursos relacionados pela categoria
- ✅ Cursos simulados armazenados no banco de dados
- ✅ Exibição como se o sistema estivesse "consultando" a plataforma Alura

---

## 🌍 Conexão com o Tema: "O Futuro do Trabalho"

Este sistema está alinhado ao tema **"O Futuro do Trabalho"** porque:

- ✅ Incentiva o **aprendizado contínuo** (reskilling e upskilling)
- ✅ Ajuda pessoas a encontrarem cursos para se adaptarem ao mercado
- ✅ Conecta habilidades com oportunidades de aprendizado
- ✅ Suporta empresas na recomendação de cursos para funcionários
- ✅ Foca em **requalificação profissional** para o futuro

### Alinhamento com ODS:
- **ODS 4:** Educação de qualidade
- **ODS 8:** Trabalho decente e crescimento econômico
- **ODS 9:** Inovação e infraestrutura

---

## 👥 Autores

Equipe Global Solution 2025 - FIAP

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.

---

## 📚 Referências

- Documentação Oracle: https://docs.oracle.com/
- Spring Boot: https://spring.io/projects/spring-boot
- Next.js: https://nextjs.org/docs
- Padrões de Modelagem de Dados
- Boas Práticas de Banco de Dados Relacionais
- Tema: O Futuro do Trabalho - Global Solution 2025
