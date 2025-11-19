-- =====================================================
-- Script de Criação do Banco de Dados Oracle
-- Catálogo de Habilidades Integrado à Alura
-- Global Solution 2025 - FIAP
-- Tema: O Futuro do Trabalho
-- =====================================================

-- Nota: Este script foi adaptado para Oracle Database
-- Execute como usuário com privilégios adequados (DBA ou usuário com CREATE TABLE)
--
-- IMPORTANTE: Este script limpa objetos existentes antes de criar novos
-- Se você não quiser que isso aconteça, comente a seção de limpeza abaixo

-- =====================================================
-- LIMPEZA DE OBJETOS EXISTENTES (se necessário)
-- =====================================================
-- Descomente a seção abaixo se quiser limpar objetos existentes antes de criar

BEGIN
    -- Dropar triggers primeiro
    FOR cur_rec IN (SELECT object_name FROM user_objects WHERE object_type = 'TRIGGER' AND object_name LIKE 'TRG_%') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP TRIGGER ' || cur_rec.object_name;
        EXCEPTION
            WHEN OTHERS THEN NULL;
        END;
    END LOOP;
    
    -- Dropar procedures
    FOR cur_rec IN (SELECT object_name FROM user_objects WHERE object_type = 'PROCEDURE' AND object_name LIKE 'SP_%') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP PROCEDURE ' || cur_rec.object_name;
        EXCEPTION
            WHEN OTHERS THEN NULL;
        END;
    END LOOP;
    
    -- Dropar tabelas (em ordem para respeitar foreign keys)
    BEGIN
        EXECUTE IMMEDIATE 'DROP TABLE GS_META_MENSAL CASCADE CONSTRAINTS';
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'DROP TABLE GS_USUARIO_CURSO CASCADE CONSTRAINTS';
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'DROP TABLE GS_HABILIDADE CASCADE CONSTRAINTS';
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'DROP TABLE GS_CURSO CASCADE CONSTRAINTS';
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
    
    BEGIN
        EXECUTE IMMEDIATE 'DROP TABLE GS_USUARIO CASCADE CONSTRAINTS';
    EXCEPTION
        WHEN OTHERS THEN NULL;
    END;
END;
/

-- =====================================================
-- Tabela: GS_USUARIO
-- Descrição: Armazena usuários do sistema
-- =====================================================
CREATE TABLE GS_USUARIO (
    id_usuario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    xp_total NUMBER DEFAULT 0 NOT NULL,
    meta_cursos_mensal NUMBER DEFAULT 10 NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

-- Nota: O índice para email é criado automaticamente pela constraint UNIQUE
-- Não é necessário criar manualmente

-- =====================================================
-- Tabela: GS_HABILIDADE
-- Descrição: Armazena habilidades cadastradas no sistema
-- =====================================================
CREATE TABLE GS_HABILIDADE (
    id_habilidade NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario NUMBER,
    nome VARCHAR2(100) NOT NULL,
    categoria VARCHAR2(50) NOT NULL,
    descricao CLOB,
    nivel VARCHAR2(20),
    progresso_percentual NUMBER DEFAULT 0,
    CONSTRAINT fk_habilidade_usuario FOREIGN KEY (id_usuario) 
        REFERENCES GS_USUARIO(id_usuario) ON DELETE SET NULL,
    CONSTRAINT ck_habilidade_nivel CHECK (nivel IN ('Iniciante', 'Intermediário', 'Avançado')),
    CONSTRAINT ck_habilidade_progresso CHECK (progresso_percentual >= 0 AND progresso_percentual <= 100)
);

-- Índices para GS_HABILIDADE
CREATE INDEX idx_habilidade_categoria ON GS_HABILIDADE(categoria);
CREATE INDEX idx_habilidade_nome ON GS_HABILIDADE(nome);
CREATE INDEX idx_habilidade_usuario ON GS_HABILIDADE(id_usuario);

-- =====================================================
-- Tabela: GS_CURSO
-- Descrição: Armazena cursos simulados da Alura
-- Relacionamento: Relaciona-se com GS_HABILIDADE pela categoria
-- =====================================================
CREATE TABLE GS_CURSO (
    id_curso NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(200) NOT NULL,
    categoria VARCHAR2(50) NOT NULL,
    link VARCHAR2(500),
    descricao CLOB,
    duracao_horas NUMBER,
    nivel VARCHAR2(20),
    id_alura VARCHAR2(100),
    slug VARCHAR2(200),
    instrutor VARCHAR2(200),
    imagem_url VARCHAR2(500),
    categoria_alura VARCHAR2(100),
    origem VARCHAR2(20) DEFAULT 'MANUAL',
    CONSTRAINT ck_curso_nivel CHECK (nivel IN ('Iniciante', 'Intermediário', 'Avançado')),
    CONSTRAINT ck_curso_origem CHECK (origem IN ('ALURA', 'MANUAL'))
);

-- Índices para GS_CURSO
CREATE INDEX idx_curso_categoria ON GS_CURSO(categoria);
CREATE INDEX idx_curso_nome ON GS_CURSO(nome);
CREATE INDEX idx_curso_id_alura ON GS_CURSO(id_alura);
CREATE INDEX idx_curso_origem ON GS_CURSO(origem);

-- =====================================================
-- Tabela: GS_USUARIO_CURSO
-- Descrição: Relacionamento entre usuário e curso com progresso
-- =====================================================
CREATE TABLE GS_USUARIO_CURSO (
    id_usuario_curso NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario NUMBER NOT NULL,
    id_curso NUMBER,
    id_curso_alura VARCHAR2(100),
    progresso_percentual NUMBER DEFAULT 0,
    data_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_conclusao TIMESTAMP,
    concluido CHAR(1) DEFAULT 'N',
    xp_ganho NUMBER DEFAULT 0,
    CONSTRAINT fk_usuario_curso_usuario FOREIGN KEY (id_usuario) 
        REFERENCES GS_USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_curso_curso FOREIGN KEY (id_curso) 
        REFERENCES GS_CURSO(id_curso) ON DELETE SET NULL,
    CONSTRAINT ck_usuario_curso_progresso CHECK (progresso_percentual >= 0 AND progresso_percentual <= 100),
    CONSTRAINT ck_usuario_curso_concluido CHECK (concluido IN ('S', 'N')),
    CONSTRAINT ck_usuario_curso_curso CHECK (
        (id_curso IS NOT NULL AND id_curso_alura IS NULL) OR 
        (id_curso IS NULL AND id_curso_alura IS NOT NULL)
    )
);

-- Índices para GS_USUARIO_CURSO
CREATE INDEX idx_usuario_curso_usuario ON GS_USUARIO_CURSO(id_usuario);
CREATE INDEX idx_usuario_curso_curso ON GS_USUARIO_CURSO(id_curso);
CREATE INDEX idx_usuario_curso_alura ON GS_USUARIO_CURSO(id_curso_alura);
CREATE INDEX idx_usuario_curso_concluido ON GS_USUARIO_CURSO(concluido);

-- =====================================================
-- Tabela: GS_META_MENSAL
-- Descrição: Metas mensais de cursos por usuário
-- =====================================================
CREATE TABLE GS_META_MENSAL (
    id_meta NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario NUMBER NOT NULL,
    mes NUMBER NOT NULL,
    ano NUMBER NOT NULL,
    meta_cursos NUMBER NOT NULL,
    cursos_concluidos NUMBER DEFAULT 0,
    CONSTRAINT fk_meta_usuario FOREIGN KEY (id_usuario) 
        REFERENCES GS_USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT ck_meta_mes CHECK (mes >= 1 AND mes <= 12),
    CONSTRAINT uk_meta_usuario_mes_ano UNIQUE (id_usuario, mes, ano)
);

-- Definir valor padrão para meta_cursos usando ALTER TABLE
-- (Oracle não permite DEFAULT seguido de NOT NULL em algumas versões)
ALTER TABLE GS_META_MENSAL MODIFY meta_cursos DEFAULT 10;

-- Índices para GS_META_MENSAL
CREATE INDEX idx_meta_usuario ON GS_META_MENSAL(id_usuario);
CREATE INDEX idx_meta_mes_ano ON GS_META_MENSAL(mes, ano);

-- =====================================================
-- TRIGGERS E PROCEDURES
-- =====================================================

-- Trigger para atualizar XP total do usuário quando curso é concluído
CREATE OR REPLACE TRIGGER trg_atualizar_xp_usuario
AFTER UPDATE OF concluido ON GS_USUARIO_CURSO
FOR EACH ROW
WHEN (NEW.concluido = 'S' AND OLD.concluido = 'N')
BEGIN
    UPDATE GS_USUARIO
    SET xp_total = xp_total + NVL(:NEW.xp_ganho, 0)
    WHERE id_usuario = :NEW.id_usuario;
END;
/

-- Trigger para atualizar contador de cursos concluídos na meta mensal
CREATE OR REPLACE TRIGGER trg_atualizar_meta_mensal
AFTER UPDATE OF concluido ON GS_USUARIO_CURSO
FOR EACH ROW
WHEN (NEW.concluido = 'S' AND OLD.concluido = 'N')
BEGIN
    UPDATE GS_META_MENSAL
    SET cursos_concluidos = cursos_concluidos + 1
    WHERE id_usuario = :NEW.id_usuario
      AND mes = EXTRACT(MONTH FROM :NEW.data_conclusao)
      AND ano = EXTRACT(YEAR FROM :NEW.data_conclusao);
END;
/

-- Procedure para calcular progresso de habilidade baseado nos cursos
CREATE OR REPLACE PROCEDURE sp_calcular_progresso_habilidade(
    p_id_habilidade IN NUMBER
) AS
    v_progresso_medio NUMBER;
BEGIN
    SELECT NVL(AVG(uc.progresso_percentual), 0)
    INTO v_progresso_medio
    FROM GS_USUARIO_CURSO uc
    INNER JOIN GS_CURSO c ON uc.id_curso = c.id_curso
    INNER JOIN GS_HABILIDADE h ON c.categoria = h.categoria
    WHERE h.id_habilidade = p_id_habilidade
      AND uc.id_usuario = (SELECT id_usuario FROM GS_HABILIDADE WHERE id_habilidade = p_id_habilidade);
    
    UPDATE GS_HABILIDADE
    SET progresso_percentual = v_progresso_medio
    WHERE id_habilidade = p_id_habilidade;
END;
/

-- =====================================================
-- Dados de Exemplo
-- =====================================================

-- Inserir usuários de exemplo
-- Nota: Os password_hash abaixo são apenas exemplos. Em produção, use bcrypt no backend Java
-- Hash de exemplo para senha "senha123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO GS_USUARIO (nome, email, password_hash) VALUES
('João Silva', 'joao@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO GS_USUARIO (nome, email, password_hash) VALUES
('Maria Santos', 'maria@empresa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO GS_USUARIO (nome, email, password_hash) VALUES
('Tech Solutions Ltda', 'contato@techsolutions.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO GS_USUARIO (nome, email, password_hash) VALUES
('Ana Costa', 'ana.costa@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO GS_USUARIO (nome, email, password_hash) VALUES
('Instituto de Tecnologia', 'contato@institutotech.edu.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Inserir habilidades de exemplo
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(1, 'Java', 'Tecnologia', 'Linguagem de programação orientada a objetos, amplamente usada no desenvolvimento de aplicações empresariais');
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(1, 'Comunicação', 'Soft Skill', 'Habilidade de comunicação assertiva e eficaz em diferentes contextos profissionais');
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(2, 'UX Design', 'Design', 'Design de experiência do usuário, focado em criar interfaces intuitivas e agradáveis');
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(2, 'Liderança', 'Soft Skill', 'Habilidades de liderança de equipes e gestão de pessoas');
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(3, 'Python', 'Tecnologia', 'Linguagem de programação versátil, muito usada em ciência de dados e automação');
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(4, 'React', 'Tecnologia', 'Biblioteca JavaScript para construção de interfaces de usuário');
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(4, 'Gestão de Projetos', 'Negócios', 'Metodologias e ferramentas para gerenciamento eficiente de projetos');
INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao) VALUES
(5, 'Marketing Digital', 'Marketing', 'Estratégias e técnicas de marketing na era digital');

-- Inserir cursos de exemplo (simulando cursos da Alura)
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Java e Orientação a Objetos', 'Tecnologia', 'https://www.alura.com.br/curso-online-praticando-java-orientacao-objetos-classes-atributos-metodos', 'Aprenda Java do zero com foco em orientação a objetos', 4, 'Iniciante');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Java: trabalhando com Collections', 'Tecnologia', 'https://www.alura.com.br/curso-online-java-collections', 'Domine Collections e APIs do Java', 20, 'Intermediário');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Comunicação Assertiva', 'Soft Skill', 'https://www.alura.com.br/curso-online-comunicacao-assertiva-reduzindo-conflitos-e-frustracaoes', 'Aprenda a se comunicar de forma clara, direta e respeitosa', 6, 'Intermediário');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Oratória: técnicas de apresentação', 'Soft Skill', 'https://www.alura.com.br/curso-online-oratoria-supere-desafios-confianca', 'Desenvolva habilidades de apresentação e oratória', 6, 'Iniciante');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('UX Design: do básico ao avançado', 'Design', 'https://www.alura.com.br/curso-online-figma-conhecendo-a-ferramenta', 'Curso completo de UX Design com projetos práticos', 10, 'Avançado');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Figma: design de interfaces', 'Design', 'https://www.alura.com.br/curso-online-figma', 'Aprenda a criar interfaces profissionais no Figma', 30, 'Intermediário');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Liderança e Gestão de Equipes', 'Soft Skill', 'https://www.alura.com.br/curso-online-lideranca', 'Desenvolva habilidades de liderança e gestão de pessoas', 20, 'Intermediário');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Gestão Ágil: Scrum e Kanban', 'Negócios', 'https://www.alura.com.br/curso-online-gestao-agil', 'Metodologias ágeis para gestão de projetos', 15, 'Iniciante');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Python para Data Science', 'Tecnologia', 'https://www.alura.com.br/curso-online-python-data-science', 'Python aplicado à ciência de dados e análise', 50, 'Intermediário');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('React: desenvolvendo com JavaScript', 'Tecnologia', 'https://www.alura.com.br/curso-online-react', 'Construa interfaces modernas com React', 35, 'Intermediário');
INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel) VALUES
('Marketing Digital: estratégias e métricas', 'Marketing', 'https://www.alura.com.br/curso-online-marketing-digital', 'Estratégias completas de marketing digital', 25, 'Intermediário');

-- Criar metas mensais para usuários de exemplo (mês atual)
INSERT INTO GS_META_MENSAL (id_usuario, mes, ano, meta_cursos, cursos_concluidos)
SELECT 
    id_usuario,
    EXTRACT(MONTH FROM CURRENT_TIMESTAMP),
    EXTRACT(YEAR FROM CURRENT_TIMESTAMP),
    10,
    0
FROM GS_USUARIO;

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
-- FROM GS_HABILIDADE h
-- INNER JOIN GS_CURSO c ON h.categoria = c.categoria
-- WHERE h.id_habilidade = 1
-- ORDER BY c.nome;

-- Consulta 2: Buscar cursos por categoria de habilidade
-- SELECT 
--     c.nome AS curso,
--     c.link,
--     c.duracao_horas,
--     c.nivel
-- FROM GS_CURSO c
-- WHERE c.categoria = (
--     SELECT categoria 
--     FROM GS_HABILIDADE 
--     WHERE id_habilidade = 1
-- )
-- ORDER BY c.nome;

-- Consulta 3: Listar todas as habilidades com contagem de cursos
-- SELECT 
--     h.nome AS habilidade,
--     h.categoria,
--     COUNT(c.id_curso) AS total_cursos
-- FROM GS_HABILIDADE h
-- LEFT JOIN GS_CURSO c ON h.categoria = c.categoria
-- GROUP BY h.id_habilidade, h.nome, h.categoria
-- ORDER BY total_cursos DESC;

-- =====================================================
-- Fim do Script
-- =====================================================
