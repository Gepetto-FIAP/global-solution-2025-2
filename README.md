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

---

## 📊 Diagrama de Entidade-Relacionamento (DER)

### Entidades

O modelo de dados foi projetado com **3 entidades principais**, atendendo o requisito mínimo de **3 entidades com relacionamento**:

1. **USUARIO** - Gerencia usuários do sistema (Estudante, Profissional, Empresa)
2. **HABILIDADE** - Gerencia habilidades cadastradas (ex: Java, Comunicação, UX Design)
3. **CURSO** - Armazena cursos simulados da Alura relacionados às habilidades

### Relacionamentos

- ✅ **USUARIO → HABILIDADE** (1:N) - Usuário cadastra habilidades
- ✅ **HABILIDADE ↔ CURSO** (N:M) - Relacionamento por categoria
  - Uma habilidade pode estar relacionada a vários cursos da mesma categoria
  - Um curso pode estar relacionado a várias habilidades da mesma categoria
  - Relacionamento baseado na correspondência do campo `categoria`
  - Consulta SQL: `WHERE habilidade.categoria = curso.categoria`

---

## 📁 Estrutura de Arquivos

```
globalsolution2/
├── README.md                              # Este arquivo
├── diagrama_entidade_relacionamento.md      # Documentação completa do DER
├── diagrama_visual.txt                     # Diagrama visual em texto
└── script_banco_dados.sql                 # Script SQL de criação do banco
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

---

## 🚀 Como Usar

### 1. Criar o Banco de Dados

Execute o script SQL fornecido no Oracle:

```bash
sqlplus usuario/senha@database @script_banco_dados.sql
```

Ou execute diretamente no SQL*Plus ou SQL Developer:

```sql
@script_banco_dados.sql
```

**Nota:** Certifique-se de ter privilégios adequados (CREATE TABLE, CREATE INDEX) no schema Oracle.

### 2. Verificar as Tabelas

```sql
-- No Oracle, liste as tabelas do schema atual
SELECT table_name FROM user_tables ORDER BY table_name;

-- Ou veja todas as tabelas
SELECT table_name FROM all_tables WHERE owner = USER ORDER BY table_name;
```

### 3. Consultas Úteis

#### Listar habilidades com cursos relacionados:
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

#### Buscar cursos por categoria:
```sql
SELECT 
    c.nome AS curso,
    c.link,
    c.duracao_horas,
    c.nivel
FROM CURSO c
WHERE c.categoria = 'Tecnologia'
ORDER BY c.nome;
```

#### Listar habilidades com contagem de cursos:
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

---

## 📝 Requisitos Atendidos

### Requisitos Técnicos
- ✅ **Mínimo 3 entidades:** USUARIO, HABILIDADE, CURSO
- ✅ **Relacionamentos:** 
  - Relacionamento 1:N (USUARIO → HABILIDADE)
  - Relacionamento N:M por categoria (HABILIDADE ↔ CURSO)
- ✅ **Chaves Primárias:** Todas as entidades possuem PK
- ✅ **Normalização:** Modelo normalizado

### Entregas do Projeto
- ✅ **Banco de Dados:** Script SQL completo (`script_banco_dados.sql`)
- ✅ **Diagrama ER:** Documentação completa (`diagrama_entidade_relacionamento.md`)
- ✅ **Documentação:** README com instruções de uso

---

## 💡 Funcionalidades do Sistema

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

## 🛠️ Stack Tecnológica

- **Backend:** Java (JDBC + POO)
- **Banco de Dados:** Oracle Database
- **Frontend:** HTML + CSS + JavaScript (simples)
- **Arquitetura:** MVC (Model-View-Controller)

---

## 📚 Exemplos de Uso

### Exemplo 1: Cadastrar Habilidade
```
Nome: Java
Categoria: Tecnologia
Descrição: Linguagem de programação orientada a objetos
```

### Exemplo 2: Ver Cursos Sugeridos
Ao selecionar a habilidade "Java" (categoria: Tecnologia), o sistema mostra:
- Java e Orientação a Objetos - Alura
- Java: trabalhando com Collections - Alura
- (todos os cursos da categoria "Tecnologia")

---

## 👥 Autores

Equipe Global Solution 2025 - FIAP

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.

---

## 📚 Referências

- Documentação MySQL: https://dev.mysql.com/doc/
- Padrões de Modelagem de Dados
- Boas Práticas de Banco de Dados Relacionais
- Tema: O Futuro do Trabalho - Global Solution 2025
