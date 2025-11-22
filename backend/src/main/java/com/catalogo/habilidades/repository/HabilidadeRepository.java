package com.catalogo.habilidades.repository;

import com.catalogo.habilidades.config.PersistenceConfig;
import com.catalogo.habilidades.model.Habilidade;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabilidadeRepository {
    
    /**
     * Busca uma habilidade por ID
     */
    public Optional<Habilidade> findById(Long id) {
        String sql = "SELECT id_habilidade, id_usuario, nome, categoria_slug, subcategoria_slug, nivel, data_criacao " +
                     "FROM GS_HABILIDADE_USUARIO WHERE id_habilidade = ?";
        
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
     * Busca todas as habilidades de um usuário
     */
    public List<Habilidade> findByUsuarioId(Long idUsuario) {
        String sql = "SELECT id_habilidade, id_usuario, nome, categoria_slug, subcategoria_slug, nivel, data_criacao " +
                     "FROM GS_HABILIDADE_USUARIO WHERE id_usuario = ? ORDER BY data_criacao DESC";
        
        List<Habilidade> habilidades = new ArrayList<>();
        
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
     * Salva uma habilidade
     */
    public Habilidade save(Habilidade habilidade) {
        if (habilidade.getIdHabilidade() == null) {
            return insert(habilidade);
        } else {
            return update(habilidade);
        }
    }
    
    private Habilidade insert(Habilidade habilidade) {
        String sql = "INSERT INTO GS_HABILIDADE_USUARIO (id_usuario, nome, categoria_slug, subcategoria_slug, nivel, data_criacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_habilidade"})) {
            
            stmt.setLong(1, habilidade.getIdUsuario());
            stmt.setString(2, habilidade.getNome());
            stmt.setString(3, habilidade.getCategoriaSlug());
            stmt.setString(4, habilidade.getSubcategoriaSlug());
            stmt.setString(5, habilidade.getNivel());
            stmt.setTimestamp(6, Timestamp.valueOf(habilidade.getDataCriacao()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        habilidade.setIdHabilidade(generatedKeys.getLong(1));
                    }
                }
            }
            
            return habilidade;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir habilidade", e);
        }
    }
    
    private Habilidade update(Habilidade habilidade) {
        String sql = "UPDATE GS_HABILIDADE_USUARIO SET nome = ?, categoria_slug = ?, subcategoria_slug = ?, nivel = ? " +
                     "WHERE id_habilidade = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, habilidade.getNome());
            stmt.setString(2, habilidade.getCategoriaSlug());
            stmt.setString(3, habilidade.getSubcategoriaSlug());
            stmt.setString(4, habilidade.getNivel());
            stmt.setLong(5, habilidade.getIdHabilidade());
            
            stmt.executeUpdate();
            
            return habilidade;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar habilidade", e);
        }
    }
    
    /**
     * Deleta uma habilidade
     */
    public void delete(Long id) {
        String sql = "DELETE FROM GS_HABILIDADE_USUARIO WHERE id_habilidade = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar habilidade", e);
        }
    }
    
    private Habilidade mapResultSetToHabilidade(ResultSet rs) throws SQLException {
        Habilidade habilidade = new Habilidade();
        habilidade.setIdHabilidade(rs.getLong("id_habilidade"));
        habilidade.setIdUsuario(rs.getLong("id_usuario"));
        habilidade.setNome(rs.getString("nome"));
        habilidade.setCategoriaSlug(rs.getString("categoria_slug"));
        habilidade.setSubcategoriaSlug(rs.getString("subcategoria_slug"));
        habilidade.setNivel(rs.getString("nivel"));
        
        Timestamp timestamp = rs.getTimestamp("data_criacao");
        if (timestamp != null) {
            habilidade.setDataCriacao(timestamp.toLocalDateTime());
        }
        
        return habilidade;
    }
}

