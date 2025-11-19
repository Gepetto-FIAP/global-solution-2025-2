package com.catalogo.habilidades.repository;

import com.catalogo.habilidades.config.PersistenceConfig;
import com.catalogo.habilidades.model.UsuarioCurso;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioCursoRepository {
    
    /**
     * Busca todos os cursos de um usuário
     */
    public List<UsuarioCurso> findByUsuarioId(Long idUsuario) {
        List<UsuarioCurso> usuarioCursos = new ArrayList<>();
        String sql = "SELECT id_usuario_curso, id_usuario, id_curso, id_curso_alura, progresso_percentual, " +
                     "data_inicio, data_conclusao, concluido, xp_ganho " +
                     "FROM GS_USUARIO_CURSO WHERE id_usuario = ? ORDER BY data_inicio DESC";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarioCursos.add(mapResultSetToUsuarioCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cursos do usuário", e);
        }
        
        return usuarioCursos;
    }
    
    /**
     * Busca curso do usuário por ID do curso
     */
    public Optional<UsuarioCurso> findByUsuarioIdAndCursoId(Long idUsuario, Long idCurso) {
        String sql = "SELECT id_usuario_curso, id_usuario, id_curso, id_curso_alura, progresso_percentual, " +
                     "data_inicio, data_conclusao, concluido, xp_ganho " +
                     "FROM GS_USUARIO_CURSO WHERE id_usuario = ? AND id_curso = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            stmt.setLong(2, idCurso);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuarioCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curso do usuário", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca curso do usuário por ID Alura
     */
    public Optional<UsuarioCurso> findByUsuarioIdAndCursoAluraId(Long idUsuario, String idCursoAlura) {
        String sql = "SELECT id_usuario_curso, id_usuario, id_curso, id_curso_alura, progresso_percentual, " +
                     "data_inicio, data_conclusao, concluido, xp_ganho " +
                     "FROM GS_USUARIO_CURSO WHERE id_usuario = ? AND id_curso_alura = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            stmt.setString(2, idCursoAlura);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuarioCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curso do usuário por ID Alura", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca cursos concluídos de um usuário
     */
    public List<UsuarioCurso> findConcluidosByUsuarioId(Long idUsuario) {
        List<UsuarioCurso> usuarioCursos = new ArrayList<>();
        String sql = "SELECT id_usuario_curso, id_usuario, id_curso, id_curso_alura, progresso_percentual, " +
                     "data_inicio, data_conclusao, concluido, xp_ganho " +
                     "FROM GS_USUARIO_CURSO WHERE id_usuario = ? AND concluido = 'S' ORDER BY data_conclusao DESC";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarioCursos.add(mapResultSetToUsuarioCurso(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cursos concluídos do usuário", e);
        }
        
        return usuarioCursos;
    }
    
    /**
     * Conta cursos concluídos no mês atual
     */
    public int countConcluidosNoMes(Long idUsuario, int mes, int ano) {
        String sql = "SELECT COUNT(*) FROM GS_USUARIO_CURSO " +
                     "WHERE id_usuario = ? AND concluido = 'S' " +
                     "AND EXTRACT(MONTH FROM data_conclusao) = ? AND EXTRACT(YEAR FROM data_conclusao) = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar cursos concluídos no mês", e);
        }
        
        return 0;
    }
    
    /**
     * Salva ou atualiza curso do usuário
     */
    public UsuarioCurso save(UsuarioCurso usuarioCurso) {
        if (usuarioCurso.getIdUsuarioCurso() == null) {
            return insert(usuarioCurso);
        } else {
            return update(usuarioCurso);
        }
    }
    
    /**
     * Insere novo curso do usuário
     */
    private UsuarioCurso insert(UsuarioCurso usuarioCurso) {
        String sql = "INSERT INTO GS_USUARIO_CURSO (id_usuario, id_curso, id_curso_alura, progresso_percentual, " +
                     "data_inicio, data_conclusao, concluido, xp_ganho) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_usuario_curso"})) {
            
            stmt.setLong(1, usuarioCurso.getIdUsuario());
            setLongOrNull(stmt, 2, usuarioCurso.getIdCurso());
            setStringOrNull(stmt, 3, usuarioCurso.getIdCursoAlura());
            stmt.setInt(4, usuarioCurso.getProgressoPercentual() != null ? usuarioCurso.getProgressoPercentual() : 0);
            
            if (usuarioCurso.getDataInicio() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(usuarioCurso.getDataInicio()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }
            
            if (usuarioCurso.getDataConclusao() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(usuarioCurso.getDataConclusao()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            
            stmt.setString(7, usuarioCurso.getConcluido() != null && usuarioCurso.getConcluido() ? "S" : "N");
            stmt.setInt(8, usuarioCurso.getXpGanho() != null ? usuarioCurso.getXpGanho() : 0);
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuarioCurso.setIdUsuarioCurso(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir curso do usuário", e);
        }
        
        return usuarioCurso;
    }
    
    /**
     * Atualiza curso do usuário
     */
    private UsuarioCurso update(UsuarioCurso usuarioCurso) {
        String sql = "UPDATE GS_USUARIO_CURSO SET progresso_percentual = ?, data_conclusao = ?, " +
                     "concluido = ?, xp_ganho = ? WHERE id_usuario_curso = ? AND id_usuario = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioCurso.getProgressoPercentual() != null ? usuarioCurso.getProgressoPercentual() : 0);
            
            if (usuarioCurso.getDataConclusao() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(usuarioCurso.getDataConclusao()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }
            
            stmt.setString(3, usuarioCurso.getConcluido() != null && usuarioCurso.getConcluido() ? "S" : "N");
            stmt.setInt(4, usuarioCurso.getXpGanho() != null ? usuarioCurso.getXpGanho() : 0);
            stmt.setLong(5, usuarioCurso.getIdUsuarioCurso());
            stmt.setLong(6, usuarioCurso.getIdUsuario());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Curso do usuário não encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar curso do usuário", e);
        }
        
        return usuarioCurso;
    }
    
    /**
     * Mapeia ResultSet para UsuarioCurso
     */
    private UsuarioCurso mapResultSetToUsuarioCurso(ResultSet rs) throws SQLException {
        UsuarioCurso usuarioCurso = new UsuarioCurso();
        usuarioCurso.setIdUsuarioCurso(rs.getLong("id_usuario_curso"));
        usuarioCurso.setIdUsuario(rs.getLong("id_usuario"));
        
        Long idCurso = rs.getLong("id_curso");
        if (!rs.wasNull()) {
            usuarioCurso.setIdCurso(idCurso);
        }
        
        usuarioCurso.setIdCursoAlura(rs.getString("id_curso_alura"));
        
        Integer progresso = rs.getInt("progresso_percentual");
        if (rs.wasNull()) {
            progresso = 0;
        }
        usuarioCurso.setProgressoPercentual(progresso);
        
        Timestamp dataInicio = rs.getTimestamp("data_inicio");
        if (dataInicio != null) {
            usuarioCurso.setDataInicio(dataInicio.toLocalDateTime());
        }
        
        Timestamp dataConclusao = rs.getTimestamp("data_conclusao");
        if (dataConclusao != null) {
            usuarioCurso.setDataConclusao(dataConclusao.toLocalDateTime());
        }
        
        String concluido = rs.getString("concluido");
        usuarioCurso.setConcluido("S".equals(concluido));
        
        Integer xpGanho = rs.getInt("xp_ganho");
        if (rs.wasNull()) {
            xpGanho = 0;
        }
        usuarioCurso.setXpGanho(xpGanho);
        
        return usuarioCurso;
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
     * Helper para setar Long ou NULL
     */
    private void setLongOrNull(PreparedStatement stmt, int index, Long value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.NUMERIC);
        } else {
            stmt.setLong(index, value);
        }
    }
}

