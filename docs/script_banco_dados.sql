-- =====================================================
-- Script de Criação do Banco de Dados Oracle
-- Catálogo de Habilidades Integrado à Alura
-- Global Solution 2025 - FIAP
-- Tema: O Futuro do Trabalho
-- =====================================================

-- Nota: Este script foi adaptado para Oracle Database
-- Execute como usuário com privilégios adequados (DBA ou usuário com CREATE TABLE)

-- =====================================================
-- Tabela: GS_USUARIO
-- Descrição: Armazena usuários do sistema
-- =====================================================
CREATE TABLE GS_USUARIO (
    id_usuario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    xp_total NUMBER DEFAULT 0 NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

-- Índice para email
CREATE INDEX idx_usuario_email ON GS_USUARIO(email);

-- =====================================================
-- Tabela: GS_HABILIDADE_USUARIO
-- Descrição: Habilidades dos usuários ligadas a categorias/subcategorias da Alura
-- =====================================================
CREATE TABLE GS_HABILIDADE_USUARIO (
    id_habilidade NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario NUMBER NOT NULL,
    nome VARCHAR2(100) NOT NULL,
    categoria_slug VARCHAR2(100) NOT NULL,
    subcategoria_slug VARCHAR2(100),
    nivel VARCHAR2(20) CHECK (nivel IN ('Iniciante', 'Intermediário', 'Avançado', 'Expert')),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_habilidade_usuario FOREIGN KEY (id_usuario) 
        REFERENCES GS_USUARIO(id_usuario) ON DELETE CASCADE
);

-- Índices para GS_HABILIDADE_USUARIO
CREATE INDEX idx_habilidade_usuario_id ON GS_HABILIDADE_USUARIO(id_usuario);
CREATE INDEX idx_habilidade_categoria ON GS_HABILIDADE_USUARIO(categoria_slug);
CREATE INDEX idx_habilidade_subcategoria ON GS_HABILIDADE_USUARIO(subcategoria_slug);

-- =====================================================
-- Tabela: GS_CURSO_INSCRICAO
-- Descrição: Inscrições dos usuários em cursos da Alura
-- =====================================================
CREATE TABLE GS_CURSO_INSCRICAO (
    id_inscricao NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario NUMBER NOT NULL,
    id_habilidade NUMBER,
    curso_slug VARCHAR2(200) NOT NULL,
    curso_nome VARCHAR2(500) NOT NULL,
    tempo_estimado NUMBER NOT NULL,
    horas_estudadas NUMBER DEFAULT 0 NOT NULL,
    completado NUMBER(1) DEFAULT 0 NOT NULL,
    data_inscricao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_conclusao TIMESTAMP,
    CONSTRAINT fk_inscricao_usuario FOREIGN KEY (id_usuario) 
        REFERENCES GS_USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_inscricao_habilidade FOREIGN KEY (id_habilidade) 
        REFERENCES GS_HABILIDADE_USUARIO(id_habilidade) ON DELETE SET NULL,
    CONSTRAINT chk_completado CHECK (completado IN (0, 1))
);

-- Índices para GS_CURSO_INSCRICAO
CREATE INDEX idx_inscricao_usuario ON GS_CURSO_INSCRICAO(id_usuario);
CREATE INDEX idx_inscricao_habilidade ON GS_CURSO_INSCRICAO(id_habilidade);
CREATE INDEX idx_inscricao_completado ON GS_CURSO_INSCRICAO(completado);
CREATE INDEX idx_inscricao_curso_slug ON GS_CURSO_INSCRICAO(curso_slug);

-- =====================================================
-- Dados de Exemplo
-- =====================================================

-- Inserir usuários de exemplo
-- Nota: Os password_hash abaixo são apenas exemplos. Em produção, use bcrypt no backend Java
-- Hash de exemplo para senha "senha123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO GS_USUARIO (nome, email, password_hash, xp_total) VALUES
('João Silva', 'joao@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 150);
INSERT INTO GS_USUARIO (nome, email, password_hash, xp_total) VALUES
('Maria Santos', 'maria@empresa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 200);
INSERT INTO GS_USUARIO (nome, email, password_hash, xp_total) VALUES
('Tech Solutions Ltda', 'contato@techsolutions.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 500);
INSERT INTO GS_USUARIO (nome, email, password_hash, xp_total) VALUES
('Ana Costa', 'ana.costa@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 75);
INSERT INTO GS_USUARIO (nome, email, password_hash, xp_total) VALUES
('Instituto de Tecnologia', 'contato@institutotech.edu.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 300);

-- Inserir habilidades de exemplo vinculadas a categorias da Alura
INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel) VALUES
(1, 'Java e Spring Boot', 'programacao', 'java', 'Intermediário');
INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel) VALUES
(1, 'Python para Data Science', 'data-science', 'python', 'Avançado');
INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel) VALUES
(2, 'React e TypeScript', 'front-end', 'react', 'Avançado');
INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel) VALUES
(2, 'UX Design', 'design-ux', 'ux', 'Intermediário');
INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel) VALUES
(3, 'Machine Learning', 'data-science', 'machine-learning', 'Avançado');
INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel) VALUES
(4, 'DevOps e Cloud', 'devops', 'cloud', 'Intermediário');
INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel) VALUES
(4, 'Gestão Ágil', 'inovacao-gestao', 'agile', 'Avançado');

-- Inserir inscrições de exemplo em cursos
INSERT INTO GS_CURSO_INSCRICAO (id_usuario, id_habilidade, curso_slug, curso_nome, tempo_estimado, horas_estudadas, completado) VALUES
(1, 1, 'java-jre-jdk-compilar-executar', 'Java JRE e JDK: compile e execute o seu programa', 8, 8, 1);
INSERT INTO GS_CURSO_INSCRICAO (id_usuario, id_habilidade, curso_slug, curso_nome, tempo_estimado, horas_estudadas, completado, data_conclusao) VALUES
(1, 2, 'python-introducao-orientacao-objetos', 'Python: introdução à orientação a objetos', 6, 6, 1, CURRENT_TIMESTAMP);
INSERT INTO GS_CURSO_INSCRICAO (id_usuario, id_habilidade, curso_slug, curso_nome, tempo_estimado, horas_estudadas, completado) VALUES
(2, 3, 'react-function-components', 'React: desenvolvendo com JavaScript', 10, 5, 0);
INSERT INTO GS_CURSO_INSCRICAO (id_usuario, id_habilidade, curso_slug, curso_nome, tempo_estimado, horas_estudadas, completado) VALUES
(2, 4, 'ux-o-que-e-experiencia-de-usuario', 'UX: o que é experiência de usuário', 6, 3, 0);

-- Confirmar as transações
COMMIT;

-- =====================================================
-- Consultas de Exemplo
-- =====================================================

-- Consulta 1: Listar habilidades do usuário com progresso em cursos
-- SELECT 
--     h.nome AS habilidade,
--     h.categoria_slug,
--     h.subcategoria_slug,
--     h.nivel,
--     COUNT(ci.id_inscricao) AS total_cursos,
--     SUM(CASE WHEN ci.completado = 1 THEN 1 ELSE 0 END) AS cursos_completos
-- FROM GS_HABILIDADE_USUARIO h
-- LEFT JOIN GS_CURSO_INSCRICAO ci ON h.id_habilidade = ci.id_habilidade
-- WHERE h.id_usuario = 1
-- GROUP BY h.id_habilidade, h.nome, h.categoria_slug, h.subcategoria_slug, h.nivel
-- ORDER BY h.data_criacao DESC;

-- Consulta 2: Dashboard do usuário
-- SELECT 
--     u.nome,
--     u.email,
--     u.xp_total,
--     COUNT(DISTINCT h.id_habilidade) AS total_habilidades,
--     COUNT(ci.id_inscricao) AS total_inscricoes,
--     SUM(CASE WHEN ci.completado = 1 THEN 1 ELSE 0 END) AS cursos_completos,
--     SUM(ci.horas_estudadas) AS horas_totais
-- FROM GS_USUARIO u
-- LEFT JOIN GS_HABILIDADE_USUARIO h ON u.id_usuario = h.id_usuario
-- LEFT JOIN GS_CURSO_INSCRICAO ci ON u.id_usuario = ci.id_usuario
-- WHERE u.id_usuario = 1
-- GROUP BY u.id_usuario, u.nome, u.email, u.xp_total;

-- Consulta 3: Cursos em progresso do usuário
-- SELECT 
--     ci.curso_nome,
--     ci.curso_slug,
--     ci.tempo_estimado,
--     ci.horas_estudadas,
--     ROUND((ci.horas_estudadas / ci.tempo_estimado) * 100, 2) AS progresso_percentual,
--     h.nome AS habilidade_relacionada,
--     ci.data_inscricao
-- FROM GS_CURSO_INSCRICAO ci
-- LEFT JOIN GS_HABILIDADE_USUARIO h ON ci.id_habilidade = h.id_habilidade
-- WHERE ci.id_usuario = 1 AND ci.completado = 0
-- ORDER BY ci.data_inscricao DESC;

-- =====================================================
-- Fim do Script
-- =====================================================
