package com.catalogo.habilidades.repository;

import com.catalogo.habilidades.config.PersistenceConfig;
import com.catalogo.habilidades.model.Habilidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabilidadeRepository {
    
    /**
     * Busca todas as habilidades de um usuário
     */
    public List<Habilidade> findByUsuarioId(Long idUsuario) {
        List<Habilidade> habilidades = new ArrayList<>();
        String sql = "SELECT id_habilidade, id_usuario, nome, categoria, descricao, nivel, progresso_percentual " +
                     "FROM GS_HABILIDADE WHERE id_usuario = ? ORDER BY nome";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    habilidades.add(mapResultSetToHabilidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar habilidades do usuário", e);
        }
        
        return habilidades;
    }
    
    /**
     * Busca uma habilidade por ID
     */
    public Optional<Habilidade> findById(Long id) {
        String sql = "SELECT id_habilidade, id_usuario, nome, categoria, descricao, nivel, progresso_percentual " +
                     "FROM GS_HABILIDADE WHERE id_habilidade = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToHabilidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar habilidade por ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Salva uma nova habilidade
     */
    public Habilidade save(Habilidade habilidade) {
        String sql = "INSERT INTO GS_HABILIDADE (id_usuario, nome, categoria, descricao, nivel, progresso_percentual) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_habilidade"})) {
            
            stmt.setLong(1, habilidade.getIdUsuario());
            stmt.setString(2, habilidade.getNome());
            stmt.setString(3, habilidade.getCategoria());
            setStringOrNull(stmt, 4, habilidade.getDescricao());
            setStringOrNull(stmt, 5, habilidade.getNivel());
            stmt.setInt(6, habilidade.getProgressoPercentual() != null ? habilidade.getProgressoPercentual() : 0);
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    habilidade.setIdHabilidade(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar habilidade", e);
        }
        
        return habilidade;
    }
    
    /**
     * Atualiza uma habilidade existente
     */
    public Habilidade update(Habilidade habilidade) {
        String sql = "UPDATE GS_HABILIDADE SET nome = ?, categoria = ?, descricao = ?, nivel = ?, progresso_percentual = ? " +
                     "WHERE id_habilidade = ? AND id_usuario = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, habilidade.getNome());
            stmt.setString(2, habilidade.getCategoria());
            setStringOrNull(stmt, 3, habilidade.getDescricao());
            setStringOrNull(stmt, 4, habilidade.getNivel());
            stmt.setInt(5, habilidade.getProgressoPercentual() != null ? habilidade.getProgressoPercentual() : 0);
            stmt.setLong(6, habilidade.getIdHabilidade());
            stmt.setLong(7, habilidade.getIdUsuario());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Habilidade não encontrada ou não pertence ao usuário");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar habilidade", e);
        }
        
        return habilidade;
    }
    
    /**
     * Deleta uma habilidade
     */
    public void delete(Long idHabilidade, Long idUsuario) {
        String sql = "DELETE FROM GS_HABILIDADE WHERE id_habilidade = ? AND id_usuario = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idHabilidade);
            stmt.setLong(2, idUsuario);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Habilidade não encontrada ou não pertence ao usuário");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar habilidade", e);
        }
    }
    
    /**
     * Verifica se a habilidade pertence ao usuário
     */
    public boolean belongsToUsuario(Long idHabilidade, Long idUsuario) {
        String sql = "SELECT COUNT(*) FROM GS_HABILIDADE WHERE id_habilidade = ? AND id_usuario = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idHabilidade);
            stmt.setLong(2, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar propriedade da habilidade", e);
        }
        
        return false;
    }
    
    /**
     * Atualiza o progresso de uma habilidade
     */
    public void updateProgresso(Long idHabilidade, Integer progressoPercentual) {
        String sql = "UPDATE GS_HABILIDADE SET progresso_percentual = ? WHERE id_habilidade = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, progressoPercentual);
            stmt.setLong(2, idHabilidade);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar progresso da habilidade", e);
        }
    }
    
    /**
     * Mapeia ResultSet para Habilidade
     */
    private Habilidade mapResultSetToHabilidade(ResultSet rs) throws SQLException {
        Habilidade habilidade = new Habilidade();
        habilidade.setIdHabilidade(rs.getLong("id_habilidade"));
        habilidade.setIdUsuario(rs.getLong("id_usuario"));
        habilidade.setNome(rs.getString("nome"));
        habilidade.setCategoria(rs.getString("categoria"));
        habilidade.setDescricao(rs.getString("descricao"));
        habilidade.setNivel(rs.getString("nivel"));
        
        Integer progresso = rs.getInt("progresso_percentual");
        if (rs.wasNull()) {
            progresso = 0;
        }
        habilidade.setProgressoPercentual(progresso);
        
        return habilidade;
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
}

