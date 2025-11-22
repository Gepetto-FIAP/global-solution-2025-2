package com.catalogo.habilidades.repository;

import com.catalogo.habilidades.config.PersistenceConfig;
import com.catalogo.habilidades.model.CursoInscricao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursoInscricaoRepository {
    
    /**
     * Busca uma inscrição por ID
     */
    public Optional<CursoInscricao> findById(Long id) {
        String sql = "SELECT id_inscricao, id_usuario, id_habilidade, curso_slug, curso_nome, " +
                     "tempo_estimado, horas_estudadas, completado, data_inscricao, data_conclusao " +
                     "FROM GS_CURSO_INSCRICAO WHERE id_inscricao = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToInscricao(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar inscrição por ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca todas as inscrições de um usuário
     */
    public List<CursoInscricao> findByUsuarioId(Long idUsuario) {
        String sql = "SELECT id_inscricao, id_usuario, id_habilidade, curso_slug, curso_nome, " +
                     "tempo_estimado, horas_estudadas, completado, data_inscricao, data_conclusao " +
                     "FROM GS_CURSO_INSCRICAO WHERE id_usuario = ? ORDER BY data_inscricao DESC";
        
        List<CursoInscricao> inscricoes = new ArrayList<>();
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inscricoes.add(mapResultSetToInscricao(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar inscrições do usuário", e);
        }
        
        return inscricoes;
    }
    
    /**
     * Busca inscrições em progresso de um usuário
     */
    public List<CursoInscricao> findEmProgressoByUsuarioId(Long idUsuario) {
        String sql = "SELECT id_inscricao, id_usuario, id_habilidade, curso_slug, curso_nome, " +
                     "tempo_estimado, horas_estudadas, completado, data_inscricao, data_conclusao " +
                     "FROM GS_CURSO_INSCRICAO WHERE id_usuario = ? AND completado = 0 " +
                     "ORDER BY data_inscricao DESC";
        
        List<CursoInscricao> inscricoes = new ArrayList<>();
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inscricoes.add(mapResultSetToInscricao(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar inscrições em progresso", e);
        }
        
        return inscricoes;
    }
    
    /**
     * Verifica se já existe inscrição para um curso
     */
    public boolean existsByUsuarioAndCursoSlug(Long idUsuario, String cursoSlug) {
        String sql = "SELECT COUNT(*) FROM GS_CURSO_INSCRICAO WHERE id_usuario = ? AND curso_slug = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            stmt.setString(2, cursoSlug);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar inscrição existente", e);
        }
        
        return false;
    }
    
    /**
     * Salva uma inscrição
     */
    public CursoInscricao save(CursoInscricao inscricao) {
        if (inscricao.getIdInscricao() == null) {
            return insert(inscricao);
        } else {
            return update(inscricao);
        }
    }
    
    private CursoInscricao insert(CursoInscricao inscricao) {
        String sql = "INSERT INTO GS_CURSO_INSCRICAO (id_usuario, id_habilidade, curso_slug, curso_nome, " +
                     "tempo_estimado, horas_estudadas, completado, data_inscricao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_inscricao"})) {
            
            stmt.setLong(1, inscricao.getIdUsuario());
            if (inscricao.getIdHabilidade() != null) {
                stmt.setLong(2, inscricao.getIdHabilidade());
            } else {
                stmt.setNull(2, Types.NUMERIC);
            }
            stmt.setString(3, inscricao.getCursoSlug());
            stmt.setString(4, inscricao.getCursoNome());
            stmt.setInt(5, inscricao.getTempoEstimado());
            stmt.setInt(6, inscricao.getHorasEstudadas());
            stmt.setInt(7, inscricao.getCompletado() ? 1 : 0);
            stmt.setTimestamp(8, Timestamp.valueOf(inscricao.getDataInscricao()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inscricao.setIdInscricao(generatedKeys.getLong(1));
                    }
                }
            }
            
            return inscricao;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir inscrição", e);
        }
    }
    
    private CursoInscricao update(CursoInscricao inscricao) {
        String sql = "UPDATE GS_CURSO_INSCRICAO SET horas_estudadas = ?, completado = ?, data_conclusao = ? " +
                     "WHERE id_inscricao = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, inscricao.getHorasEstudadas());
            stmt.setInt(2, inscricao.getCompletado() ? 1 : 0);
            if (inscricao.getDataConclusao() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(inscricao.getDataConclusao()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }
            stmt.setLong(4, inscricao.getIdInscricao());
            
            stmt.executeUpdate();
            
            return inscricao;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar inscrição", e);
        }
    }
    
    /**
     * Atualiza o progresso de uma inscrição
     */
    public void updateProgresso(Long idInscricao, Integer horasEstudadas) {
        String sql = "UPDATE GS_CURSO_INSCRICAO SET horas_estudadas = ? WHERE id_inscricao = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, horasEstudadas);
            stmt.setLong(2, idInscricao);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar progresso", e);
        }
    }
    
    /**
     * Marca uma inscrição como completa
     */
    public void marcarComoConcluido(Long idInscricao) {
        String sql = "UPDATE GS_CURSO_INSCRICAO SET completado = 1, data_conclusao = ? WHERE id_inscricao = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, idInscricao);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao marcar inscrição como concluída", e);
        }
    }
    
    private CursoInscricao mapResultSetToInscricao(ResultSet rs) throws SQLException {
        CursoInscricao inscricao = new CursoInscricao();
        inscricao.setIdInscricao(rs.getLong("id_inscricao"));
        inscricao.setIdUsuario(rs.getLong("id_usuario"));
        
        long idHabilidade = rs.getLong("id_habilidade");
        if (!rs.wasNull()) {
            inscricao.setIdHabilidade(idHabilidade);
        }
        
        inscricao.setCursoSlug(rs.getString("curso_slug"));
        inscricao.setCursoNome(rs.getString("curso_nome"));
        inscricao.setTempoEstimado(rs.getInt("tempo_estimado"));
        inscricao.setHorasEstudadas(rs.getInt("horas_estudadas"));
        inscricao.setCompletado(rs.getInt("completado") == 1);
        
        Timestamp dataInscricao = rs.getTimestamp("data_inscricao");
        if (dataInscricao != null) {
            inscricao.setDataInscricao(dataInscricao.toLocalDateTime());
        }
        
        Timestamp dataConclusao = rs.getTimestamp("data_conclusao");
        if (dataConclusao != null) {
            inscricao.setDataConclusao(dataConclusao.toLocalDateTime());
        }
        
        return inscricao;
    }
}

