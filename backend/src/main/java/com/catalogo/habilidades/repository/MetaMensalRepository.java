package com.catalogo.habilidades.repository;

import com.catalogo.habilidades.config.PersistenceConfig;
import com.catalogo.habilidades.model.MetaMensal;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class MetaMensalRepository {
    
    /**
     * Busca meta mensal do usuário para um mês/ano específico
     */
    public Optional<MetaMensal> findByUsuarioIdAndMesAno(Long idUsuario, int mes, int ano) {
        String sql = "SELECT id_meta, id_usuario, mes, ano, meta_cursos, cursos_concluidos " +
                     "FROM GS_META_MENSAL WHERE id_usuario = ? AND mes = ? AND ano = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMetaMensal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar meta mensal", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca ou cria meta mensal do usuário para o mês atual
     */
    public MetaMensal findOrCreateCurrentMonth(Long idUsuario) {
        LocalDate now = LocalDate.now();
        int mes = now.getMonthValue();
        int ano = now.getYear();
        
        Optional<MetaMensal> metaOpt = findByUsuarioIdAndMesAno(idUsuario, mes, ano);
        
        if (metaOpt.isPresent()) {
            return metaOpt.get();
        }
        
        // Criar nova meta para o mês atual
        MetaMensal novaMeta = new MetaMensal();
        novaMeta.setIdUsuario(idUsuario);
        novaMeta.setMes(mes);
        novaMeta.setAno(ano);
        novaMeta.setMetaCursos(10); // Padrão
        novaMeta.setCursosConcluidos(0);
        
        return save(novaMeta);
    }
    
    /**
     * Salva ou atualiza meta mensal
     */
    public MetaMensal save(MetaMensal meta) {
        if (meta.getIdMeta() == null) {
            return insert(meta);
        } else {
            return update(meta);
        }
    }
    
    /**
     * Insere nova meta mensal
     */
    private MetaMensal insert(MetaMensal meta) {
        String sql = "INSERT INTO GS_META_MENSAL (id_usuario, mes, ano, meta_cursos, cursos_concluidos) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_meta"})) {
            
            stmt.setLong(1, meta.getIdUsuario());
            stmt.setInt(2, meta.getMes());
            stmt.setInt(3, meta.getAno());
            stmt.setInt(4, meta.getMetaCursos());
            stmt.setInt(5, meta.getCursosConcluidos() != null ? meta.getCursosConcluidos() : 0);
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    meta.setIdMeta(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir meta mensal", e);
        }
        
        return meta;
    }
    
    /**
     * Atualiza meta mensal
     */
    private MetaMensal update(MetaMensal meta) {
        String sql = "UPDATE GS_META_MENSAL SET meta_cursos = ?, cursos_concluidos = ? " +
                     "WHERE id_meta = ? AND id_usuario = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, meta.getMetaCursos());
            stmt.setInt(2, meta.getCursosConcluidos() != null ? meta.getCursosConcluidos() : 0);
            stmt.setLong(3, meta.getIdMeta());
            stmt.setLong(4, meta.getIdUsuario());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Meta mensal não encontrada");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar meta mensal", e);
        }
        
        return meta;
    }
    
    /**
     * Mapeia ResultSet para MetaMensal
     */
    private MetaMensal mapResultSetToMetaMensal(ResultSet rs) throws SQLException {
        MetaMensal meta = new MetaMensal();
        meta.setIdMeta(rs.getLong("id_meta"));
        meta.setIdUsuario(rs.getLong("id_usuario"));
        meta.setMes(rs.getInt("mes"));
        meta.setAno(rs.getInt("ano"));
        meta.setMetaCursos(rs.getInt("meta_cursos"));
        
        Integer cursosConcluidos = rs.getInt("cursos_concluidos");
        if (rs.wasNull()) {
            cursosConcluidos = 0;
        }
        meta.setCursosConcluidos(cursosConcluidos);
        
        return meta;
    }
}

