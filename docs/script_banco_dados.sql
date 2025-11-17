-- =====================================================
-- Script de Criação do Banco de Dados Oracle
-- Catálogo de Habilidades Integrado à Alura
-- Global Solution 2025 - FIAP
-- Tema: O Futuro do Trabalho
-- =====================================================

-- Nota: Este script foi adaptado para Oracle Database
-- Execute como usuário com privilégios adequados (DBA ou usuário com CREATE TABLE)

-- =====================================================
-- Tabela: USUARIO
-- Descrição: Armazena usuários do sistema
-- =====================================================
CREATE TABLE USUARIO (
    id_usuario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

-- Índice para email
CREATE INDEX idx_usuario_email ON USUARIO(email);

-- =====================================================
-- Tabela: HABILIDADE
-- Descrição: Armazena habilidades cadastradas no sistema
-- =====================================================
CREATE TABLE HABILIDADE (
    id_habilidade NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario NUMBER,
    nome VARCHAR2(100) NOT NULL,
    categoria VARCHAR2(50) NOT NULL,
    descricao CLOB,
    CONSTRAINT fk_habilidade_usuario FOREIGN KEY (id_usuario) 
        REFERENCES USUARIO(id_usuario) ON DELETE SET NULL
);

-- Índices para HABILIDADE
CREATE INDEX idx_habilidade_categoria ON HABILIDADE(categoria);
CREATE INDEX idx_habilidade_nome ON HABILIDADE(nome);
CREATE INDEX idx_habilidade_usuario ON HABILIDADE(id_usuario);

-- =====================================================
-- Tabela: CURSO
-- Descrição: Armazena cursos simulados da Alura
-- Relacionamento: Relaciona-se com HABILIDADE pela categoria
-- =====================================================
CREATE TABLE CURSO (
    id_curso NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(200) NOT NULL,
    categoria VARCHAR2(50) NOT NULL,
    link VARCHAR2(500),
    descricao CLOB,
    duracao_horas NUMBER,
    nivel VARCHAR2(20) CHECK (nivel IN ('Iniciante', 'Intermediário', 'Avançado'))
);

-- Índices para CURSO
CREATE INDEX idx_curso_categoria ON CURSO(categoria);
CREATE INDEX idx_curso_nome ON CURSO(nome);

-- =====================================================
-- Dados de Exemplo
-- =====================================================

-- Inserir usuários de exemplo
-- Nota: Os password_hash abaixo são apenas exemplos. Em produção, use bcrypt no backend Java
-- Hash de exemplo para senha "senha123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO USUARIO (nome, email, password_hash) VALUES
('João Silva', 'joao@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO USUARIO (nome, email, password_hash) VALUES
('Maria Santos', 'maria@empresa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO USUARIO (nome, email, password_hash) VALUES
('Tech Solutions Ltda', 'contato@techsolutions.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO USUARIO (nome, email, password_hash) VALUES
('Ana Costa', 'ana.costa@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO USUARIO (nome, email, password_hash) VALUES
('Instituto de Tecnologia', 'contato@institutotech.edu.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Inserir habilidades de exemplo
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(1, 'Java', 'Tecnologia', 'Linguagem de programação orientada a objetos, amplamente usada no desenvolvimento de aplicações empresariais');
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(1, 'Comunicação', 'Soft Skill', 'Habilidade de comunicação assertiva e eficaz em diferentes contextos profissionais');
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(2, 'UX Design', 'Design', 'Design de experiência do usuário, focado em criar interfaces intuitivas e agradáveis');
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(2, 'Liderança', 'Soft Skill', 'Habilidades de liderança de equipes e gestão de pessoas');
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(3, 'Python', 'Tecnologia', 'Linguagem de programação versátil, muito usada em ciência de dados e automação');
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(4, 'React', 'Tecnologia', 'Biblioteca JavaScript para construção de interfaces de usuário');
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(4, 'Gestão de Projetos', 'Negócios', 'Metodologias e ferramentas para gerenciamento eficiente de projetos');
INSERT INTO HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(5, 'Marketing Digital', 'Marketing', 'Estratégias e técnicas de marketing na era digital');

-- Inserir cursos de exemplo (simulando cursos da Alura)
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Java e Orientação a Objetos', 'Tecnologia', 'https://www.alura.com.br/curso-online-praticando-java-orientacao-objetos-classes-atributos-metodos', 'Aprenda Java do zero com foco em orientação a objetos', 4, 'Iniciante');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Java: trabalhando com Collections', 'Tecnologia', 'https://www.alura.com.br/curso-online-java-collections', 'Domine Collections e APIs do Java', 20, 'Intermediário');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Comunicação Assertiva', 'Soft Skill', 'https://www.alura.com.br/curso-online-comunicacao-assertiva-reduzindo-conflitos-e-frustracaoes', 'Aprenda a se comunicar de forma clara, direta e respeitosa', 6, 'Intermediário');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Oratória: técnicas de apresentação', 'Soft Skill', 'https://www.alura.com.br/curso-online-oratoria-supere-desafios-confianca', 'Desenvolva habilidades de apresentação e oratória', 6, 'Iniciante');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('UX Design: do básico ao avançado', 'Design', 'https://www.alura.com.br/curso-online-figma-conhecendo-a-ferramenta', 'Curso completo de UX Design com projetos práticos', 10, 'Avançado');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Figma: design de interfaces', 'Design', 'https://www.alura.com.br/curso-online-figma', 'Aprenda a criar interfaces profissionais no Figma', 30, 'Intermediário');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Liderança e Gestão de Equipes', 'Soft Skill', 'https://www.alura.com.br/curso-online-lideranca', 'Desenvolva habilidades de liderança e gestão de pessoas', 20, 'Intermediário');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Gestão Ágil: Scrum e Kanban', 'Negócios', 'https://www.alura.com.br/curso-online-gestao-agil', 'Metodologias ágeis para gestão de projetos', 15, 'Iniciante');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Python para Data Science', 'Tecnologia', 'https://www.alura.com.br/curso-online-python-data-science', 'Python aplicado à ciência de dados e análise', 50, 'Intermediário');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('React: desenvolvendo com JavaScript', 'Tecnologia', 'https://www.alura.com.br/curso-online-react', 'Construa interfaces modernas com React', 35, 'Intermediário');
INSERT INTO CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Marketing Digital: estratégias e métricas', 'Marketing', 'https://www.alura.com.br/curso-online-marketing-digital', 'Estratégias completas de marketing digital', 25, 'Intermediário');

-- Confirmar as transações
COMMIT;

-- =====================================================
-- Consultas de Exemplo
-- =====================================================

-- Consulta 1: Listar habilidades com cursos relacionados
-- SELECT 
--     h.nome AS habilidade,
--     h.categoria,
--     c.nome AS curso,
--     c.link,
--     c.nivel
-- FROM HABILIDADE h
-- INNER JOIN CURSO c ON h.categoria = c.categoria
-- WHERE h.id_habilidade = 1
-- ORDER BY c.nome;

-- Consulta 2: Buscar cursos por categoria de habilidade
-- SELECT 
--     c.nome AS curso,
--     c.link,
--     c.duracao_horas,
--     c.nivel
-- FROM CURSO c
-- WHERE c.categoria = (
--     SELECT categoria 
--     FROM HABILIDADE 
--     WHERE id_habilidade = 1
-- )
-- ORDER BY c.nome;

-- Consulta 3: Listar todas as habilidades com contagem de cursos
-- SELECT 
--     h.nome AS habilidade,
--     h.categoria,
--     COUNT(c.id_curso) AS total_cursos
-- FROM HABILIDADE h
-- LEFT JOIN CURSO c ON h.categoria = c.categoria
-- GROUP BY h.id_habilidade, h.nome, h.categoria
-- ORDER BY total_cursos DESC;

-- =====================================================
-- Fim do Script
-- =====================================================
