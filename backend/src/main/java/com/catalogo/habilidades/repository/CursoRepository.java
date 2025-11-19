package com.catalogo.habilidades.repository;

import com.catalogo.habilidades.config.PersistenceConfig;
import com.catalogo.habilidades.model.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursoRepository {
    
    /**
     * Busca todos os cursos
     */
    public List<Curso> findAll() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id_curso, nome, categoria, link, descricao, duracao_horas, nivel, " +
                     "id_alura, slug, instrutor, imagem_url, categoria_alura, origem " +
                     "FROM GS_CURSO ORDER BY nome";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cursos.add(mapResultSetToCurso(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cursos", e);
        }
        
        return cursos;
    }
    
    /**
     * Busca curso por ID
     */
    public Optional<Curso> findById(Long id) {
        String sql = "SELECT id_curso, nome, categoria, link, descricao, duracao_horas, nivel, " +
                     "id_alura, slug, instrutor, imagem_url, categoria_alura, origem " +
                     "FROM GS_CURSO WHERE id_curso = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curso por ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca cursos por categoria
     */
    public List<Curso> findByCategoria(String categoria) {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id_curso, nome, categoria, link, descricao, duracao_horas, nivel, " +
                     "id_alura, slug, instrutor, imagem_url, categoria_alura, origem " +
                     "FROM GS_CURSO WHERE categoria = ? ORDER BY nome";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cursos.add(mapResultSetToCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cursos por categoria", e);
        }
        
        return cursos;
    }
    
    /**
     * Busca cursos por palavras-chave no nome ou descrição
     */
    public List<Curso> findByPalavrasChave(String palavrasChave) {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id_curso, nome, categoria, link, descricao, duracao_horas, nivel, " +
                     "id_alura, slug, instrutor, imagem_url, categoria_alura, origem " +
                     "FROM GS_CURSO " +
                     "WHERE UPPER(nome) LIKE UPPER(?) OR UPPER(descricao) LIKE UPPER(?) OR UPPER(categoria) LIKE UPPER(?) " +
                     "ORDER BY nome";
        
        String searchPattern = "%" + palavrasChave + "%";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cursos.add(mapResultSetToCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cursos por palavras-chave", e);
        }
        
        return cursos;
    }
    
    /**
     * Busca curso por ID da Alura
     */
    public Optional<Curso> findByIdAlura(String idAlura) {
        String sql = "SELECT id_curso, nome, categoria, link, descricao, duracao_horas, nivel, " +
                     "id_alura, slug, instrutor, imagem_url, categoria_alura, origem " +
                     "FROM GS_CURSO WHERE id_alura = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idAlura);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curso por ID Alura", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Salva um novo curso
     */
    public Curso save(Curso curso) {
        String sql = "INSERT INTO GS_CURSO (nome, categoria, link, descricao, duracao_horas, nivel, " +
                     "id_alura, slug, instrutor, imagem_url, categoria_alura, origem) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_curso"})) {
            
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getCategoria());
            setStringOrNull(stmt, 3, curso.getLink());
            setStringOrNull(stmt, 4, curso.getDescricao());
            setIntOrNull(stmt, 5, curso.getDuracaoHoras());
            setStringOrNull(stmt, 6, curso.getNivel());
            setStringOrNull(stmt, 7, curso.getIdAlura());
            setStringOrNull(stmt, 8, curso.getSlug());
            setStringOrNull(stmt, 9, curso.getInstrutor());
            setStringOrNull(stmt, 10, curso.getImagemUrl());
            setStringOrNull(stmt, 11, curso.getCategoriaAlura());
            stmt.setString(12, curso.getOrigem() != null ? curso.getOrigem() : "MANUAL");
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    curso.setIdCurso(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar curso", e);
        }
        
        return curso;
    }
    
    /**
     * Mapeia ResultSet para Curso
     */
    private Curso mapResultSetToCurso(ResultSet rs) throws SQLException {
        Curso curso = new Curso();
        curso.setIdCurso(rs.getLong("id_curso"));
        curso.setNome(rs.getString("nome"));
        curso.setCategoria(rs.getString("categoria"));
        curso.setLink(rs.getString("link"));
        curso.setDescricao(rs.getString("descricao"));
        
        Integer duracao = rs.getInt("duracao_horas");
        if (!rs.wasNull()) {
            curso.setDuracaoHoras(duracao);
        }
        
        curso.setNivel(rs.getString("nivel"));
        curso.setIdAlura(rs.getString("id_alura"));
        curso.setSlug(rs.getString("slug"));
        curso.setInstrutor(rs.getString("instrutor"));
        curso.setImagemUrl(rs.getString("imagem_url"));
        curso.setCategoriaAlura(rs.getString("categoria_alura"));
        curso.setOrigem(rs.getString("origem") != null ? rs.getString("origem") : "MANUAL");
        
        return curso;
    }
    
    /**
     * Helper para setar String ou NULL
     */
    private void setStringOrNull(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            stmt.setNull(index, Types.VARCHAR);
        } else {
            stmt.setString(index, value);
        }
    }
    
    /**
     * Helper para setar Integer ou NULL
     */
    private void setIntOrNull(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }
}

