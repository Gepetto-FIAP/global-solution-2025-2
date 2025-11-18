package com.catalogo.habilidades.repository;

import com.catalogo.habilidades.config.PersistenceConfig;
import com.catalogo.habilidades.model.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UsuarioRepository {
    
    /**
     * Busca um usuário por ID
     */
    public Optional<Usuario> findById(Long id) {
        String sql = "SELECT id_usuario, nome, email, password_hash, data_cadastro FROM USUARIO WHERE id_usuario = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca um usuário por email
     */
    public Optional<Usuario> findByEmail(String email) {
        String sql = "SELECT id_usuario, nome, email, password_hash, data_cadastro FROM USUARIO WHERE LOWER(email) = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Verifica se existe um usuário com o email informado
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    /**
     * Salva um usuário
     */
    public Usuario save(Usuario usuario) {
        if (usuario.getIdUsuario() == null) {
            return insert(usuario);
        } else {
            return update(usuario);
        }
    }
    
    private Usuario insert(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (nome, email, password_hash, data_cadastro) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_usuario"})) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail().toLowerCase());
            stmt.setString(3, usuario.getPasswordHash());
            stmt.setTimestamp(4, Timestamp.valueOf(usuario.getDataCadastro()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setIdUsuario(generatedKeys.getLong(1));
                    }
                }
            }
            
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário", e);
        }
    }
    
    private Usuario update(Usuario usuario) {
        String sql = "UPDATE USUARIO SET nome = ?, email = ?, password_hash = ? WHERE id_usuario = ?";
        
        try (Connection conn = PersistenceConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail().toLowerCase());
            stmt.setString(3, usuario.getPasswordHash());
            stmt.setLong(4, usuario.getIdUsuario());
            
            stmt.executeUpdate();
            
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }
    
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getLong("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPasswordHash(rs.getString("password_hash"));
        
        Timestamp timestamp = rs.getTimestamp("data_cadastro");
        if (timestamp != null) {
            usuario.setDataCadastro(timestamp.toLocalDateTime());
        }
        
        return usuario;
    }
}

