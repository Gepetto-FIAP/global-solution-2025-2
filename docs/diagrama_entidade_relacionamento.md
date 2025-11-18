# Diagrama de Entidade-Relacionamento (DER)
## Catálogo de Habilidades Integrado à Alura

### Descrição do Sistema
Sistema para cadastro de habilidades e sugestão de cursos da Alura relacionados, incentivando o aprendizado contínuo e a requalificação profissional. Alinhado ao tema **"O Futuro do Trabalho"**.

---

## Entidades

### 1. HABILIDADE
**Descrição:** Representa uma habilidade que pode ser cadastrada no sistema (ex: Java, Comunicação, UX Design).

**Atributos:**
- `id_habilidade` (PK) - INT, AUTO_INCREMENT, NOT NULL
- `id_usuario` (FK) - INT
- `nome` - VARCHAR(100), NOT NULL
- `categoria` - VARCHAR(50), NOT NULL
- `descricao` - TEXT

**Chave Primária:** id_habilidade
**Chave Estrangeira:** id_usuario → USUARIO(id_usuario)

**Exemplos de Categorias:**
- Tecnologia
- Soft Skill
- Design
- Negócios
- Marketing
- Liderança

---

### 2. CURSO
**Descrição:** Representa cursos simulados da Alura relacionados às habilidades por categoria.

**Atributos:**
- `id_curso` (PK) - INT, AUTO_INCREMENT, NOT NULL
- `nome` - VARCHAR(200), NOT NULL
- `categoria` - VARCHAR(50), NOT NULL
- `link` - VARCHAR(500)
- `descricao` - TEXT
- `duracao_horas` - INT
- `nivel` - ENUM('Iniciante', 'Intermediário', 'Avançado')

**Chave Primária:** id_curso

---

### 3. USUARIO
**Descrição:** Representa usuários do sistema que podem cadastrar habilidades e visualizar cursos sugeridos.

**Atributos:**
- `id_usuario` (PK) - NUMBER, GENERATED ALWAYS AS IDENTITY, NOT NULL
- `nome` - VARCHAR2(100), NOT NULL
- `email` - VARCHAR2(100), NOT NULL, UNIQUE
- `password_hash` - VARCHAR2(255), NOT NULL
- `data_cadastro` - TIMESTAMP, DEFAULT CURRENT_TIMESTAMP, NOT NULL

**Chave Primária:** id_usuario

**Observações:**
- `password_hash`: Senha armazenada com hash bcrypt (implementado no backend Java)
- O hash bcrypt garante segurança, armazenando apenas o hash e nunca a senha em texto plano

---

## Relacionamentos

### HABILIDADE ↔ CURSO
- **Tipo:** Relacionamento por Correspondência de Categoria (N:M)
- **Descrição:** Uma habilidade pode estar relacionada a vários cursos da mesma categoria, e um curso pode estar relacionado a várias habilidades da mesma categoria.
- **Cardinalidade:** HABILIDADE(N) ↔ CURSO(M)
- **Mecanismo:** Relacionamento baseado na correspondência do campo `categoria` (sem chave estrangeira obrigatória, apenas correspondência por nome de categoria)
- **Obrigatoriedade:** Não há FK obrigatória, o relacionamento é feito por consulta SQL baseada na categoria

### USUARIO → HABILIDADE
- **Tipo:** Relacionamento 1:N (Um para Muitos)
- **Descrição:** Um usuário pode cadastrar várias habilidades no sistema.
- **Cardinalidade:** USUARIO(1) → HABILIDADE(N)
- **Mecanismo:** Chave estrangeira `id_usuario` na tabela HABILIDADE
- **Obrigatoriedade:** FK opcional (habilidades podem existir sem usuário específico, ou sistema pode ter habilidades padrão)

---

## Diagrama Visual (Notação Crow's Foot)

```
┌─────────────────────┐
│      USUARIO        │
├─────────────────────┤
│ id_usuario (PK)     │
│ nome                │
│ email               │
│ password_hash       │
│ data_cadastro       │
└──────────┬──────────┘
           │
           │ 1
           │
           │ cadastra
           │
           │ N
┌──────────▼──────────┐
│     HABILIDADE      │
├─────────────────────┤
│ id_habilidade (PK)  │
│ id_usuario (FK)     │
│ nome                │
│ categoria           │
│ descricao           │
└──────────┬──────────┘
           │
           │ N
           │
           │ relaciona-se por categoria
           │
           │ M
┌──────────▼──────────┐
│       CURSO         │
├─────────────────────┤
│ id_curso (PK)       │
│ nome                │
│ categoria           │
│ link                │
│ descricao           │
│ duracao_horas       │
│ nivel               │
└─────────────────────┘
```

---

## Observações do Modelo

### Entidades Principais (Mínimo 3):
1. **USUARIO** - Entidade para usuários do sistema
2. **HABILIDADE** - Entidade central para cadastro de habilidades
3. **CURSO** - Entidade para cursos simulados da Alura

### Relacionamentos:
- ✅ USUARIO → HABILIDADE (1:N) - **Usuário cadastra habilidades**
- ✅ HABILIDADE ↔ CURSO (N:M) - **Relacionamento por categoria**
  - Relacionamento baseado na correspondência do campo `categoria`
  - Não há chave estrangeira obrigatória
  - Relacionamento feito por consulta SQL: `WHERE habilidade.categoria = curso.categoria`

### Melhorias Implementadas:
1. **Normalização:** Modelo em 3NF (Terceira Forma Normal)
2. **Campos Adicionais:** 
   - `duracao_horas` para informações dos cursos
   - `nivel` para classificação dos cursos
   - `descricao` em ambas as entidades para mais detalhes
3. **Flexibilidade:** Categorias como VARCHAR para permitir expansão
4. **Rastreabilidade:** IDs únicos para todas as entidades

---

## Script SQL de Criação (Oracle Database)

```sql
-- Tabela USUARIO
CREATE TABLE USUARIO (
    id_usuario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

CREATE INDEX idx_usuario_email ON USUARIO(email);

-- Tabela HABILIDADE
CREATE TABLE HABILIDADE (
    id_habilidade NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario NUMBER,
    nome VARCHAR2(100) NOT NULL,
    categoria VARCHAR2(50) NOT NULL,
    descricao CLOB,
    CONSTRAINT fk_habilidade_usuario FOREIGN KEY (id_usuario) 
        REFERENCES USUARIO(id_usuario) ON DELETE SET NULL
);

CREATE INDEX idx_habilidade_categoria ON HABILIDADE(categoria);
CREATE INDEX idx_habilidade_nome ON HABILIDADE(nome);
CREATE INDEX idx_habilidade_usuario ON HABILIDADE(id_usuario);

-- Tabela CURSO
CREATE TABLE CURSO (
    id_curso NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(200) NOT NULL,
    categoria VARCHAR2(50) NOT NULL,
    link VARCHAR2(500),
    descricao CLOB,
    duracao_horas NUMBER,
    nivel VARCHAR2(20) CHECK (nivel IN ('Iniciante', 'Intermediário', 'Avançado'))
);

CREATE INDEX idx_curso_categoria ON CURSO(categoria);
CREATE INDEX idx_curso_nome ON CURSO(nome);
```

---

## Exemplos de Consultas Úteis

### 1. Listar habilidades com cursos relacionados
```sql
SELECT 
    h.nome AS habilidade,
    h.categoria,
    c.nome AS curso,
    c.link,
    c.nivel
FROM HABILIDADE h
INNER JOIN CURSO c ON h.categoria = c.categoria
WHERE h.id_habilidade = 1
ORDER BY c.nome;
```

### 2. Buscar cursos por categoria de habilidade
```sql
SELECT 
    c.nome AS curso,
    c.link,
    c.duracao_horas,
    c.nivel
FROM CURSO c
WHERE c.categoria = (
    SELECT categoria 
    FROM HABILIDADE 
    WHERE id_habilidade = 1
)
ORDER BY c.nome;
```

### 3. Listar todas as habilidades com contagem de cursos disponíveis
```sql
SELECT 
    h.nome AS habilidade,
    h.categoria,
    COUNT(c.id_curso) AS total_cursos
FROM HABILIDADE h
LEFT JOIN CURSO c ON h.categoria = c.categoria
GROUP BY h.id_habilidade, h.nome, h.categoria
ORDER BY total_cursos DESC;
```

### 4. Filtrar habilidades por categoria
```sql
SELECT 
    nome,
    categoria,
    descricao
FROM HABILIDADE
WHERE categoria = 'Tecnologia'
ORDER BY nome;
```

---

## Dados de Exemplo

### Usuários de Exemplo:
```sql
INSERT INTO USUARIO (nome, email, tipo) VALUES
('João Silva', 'joao@email.com', 'Estudante'),
('Maria Santos', 'maria@empresa.com', 'Profissional'),
('Tech Solutions Ltda', 'contato@techsolutions.com', 'Empresa');
```

### Habilidades de Exemplo:
```sql
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(1, 'Java', 'Tecnologia', 'Linguagem de programação orientada a objetos'),
(1, 'Comunicação', 'Soft Skill', 'Habilidade de comunicação assertiva'),
(2, 'UX Design', 'Design', 'Design de experiência do usuário'),
(2, 'Liderança', 'Soft Skill', 'Habilidades de liderança de equipes'),
(3, 'Python', 'Tecnologia', 'Linguagem de programação versátil');
```

### Cursos de Exemplo (simulando Alura):
```sql
-- Links atualizados para direcionar para páginas de busca da Alura com termos específicos,
-- garantindo que sempre haverá resultados válidos relacionados ao curso
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Java e Orientação a Objetos', 'Tecnologia', 'https://www.alura.com.br/busca?q=java+orienta%C3%A7%C3%A3o+a+objetos', 'Curso completo de Java e POO', 40, 'Iniciante'),
('Comunicação Assertiva', 'Soft Skill', 'https://www.alura.com.br/busca?q=comunica%C3%A7%C3%A3o+assertiva', 'Aprenda a se comunicar de forma clara e eficaz', 10, 'Intermediário'),
('UX Design: do básico ao avançado', 'Design', 'https://www.alura.com.br/busca?q=ux+design', 'Curso completo de UX Design', 60, 'Avançado'),
('Liderança e Gestão de Equipes', 'Soft Skill', 'https://www.alura.com.br/busca?q=lideran%C3%A7a', 'Desenvolva habilidades de liderança', 20, 'Intermediário'),
('Python para Data Science', 'Tecnologia', 'https://www.alura.com.br/busca?q=python+data+science', 'Python aplicado à ciência de dados', 50, 'Intermediário');
```

---

## Considerações de Implementação

1. **Relacionamento por Categoria:** 
   - O relacionamento entre HABILIDADE e CURSO é feito pela correspondência do campo `categoria`
   - Não há chave estrangeira obrigatória
   - A consulta SQL faz o JOIN baseado na igualdade de categorias

2. **Índices:** 
   - Criados índices em `categoria` para otimizar as consultas de relacionamento
   - Índices em `nome` para buscas rápidas
   - Índices criados separadamente (Oracle não suporta índices inline)

3. **Tipos de Dados Oracle:**
   - `NUMBER` para IDs e números inteiros
   - `VARCHAR2` para strings de tamanho fixo
   - `CLOB` para textos longos (descrição)
   - `TIMESTAMP` para datas e horas
   - `IDENTITY` para auto-incremento (Oracle 12c+)

4. **Constraints:**
   - `CHECK` constraint para validar valores ENUM (tipo de usuário, nível de curso)
   - `UNIQUE` constraint para email
   - `FOREIGN KEY` com `ON DELETE SET NULL` para integridade referencial

5. **Links dos Cursos:**
   - Os links dos cursos direcionam para a página de busca da Alura com termos específicos
   - Isso garante que sempre haverá resultados válidos relacionados ao curso
   - Os links podem ser atualizados posteriormente para URLs específicas de cursos quando disponíveis
   - A estrutura de URL utilizada é: `https://www.alura.com.br/busca?q={termo-buscado}`

---

## Conexão com o Tema: "O Futuro do Trabalho"

Este sistema está alinhado ao tema **"O Futuro do Trabalho"** porque:

- ✅ Incentiva o **aprendizado contínuo** (reskilling e upskilling)
- ✅ Ajuda pessoas a encontrarem cursos para se adaptarem ao mercado
- ✅ Conecta habilidades com oportunidades de aprendizado
- ✅ Suporta empresas na recomendação de cursos para funcionários

### Alinhamento com ODS:
- **ODS 4:** Educação de qualidade
- **ODS 8:** Trabalho decente e crescimento econômico
- **ODS 9:** Inovação e infraestrutura
