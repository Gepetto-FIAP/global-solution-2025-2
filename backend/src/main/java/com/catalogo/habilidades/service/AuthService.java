package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.AuthResponse;
import com.catalogo.habilidades.dto.LoginRequest;
import com.catalogo.habilidades.dto.RegisterRequest;
import com.catalogo.habilidades.dto.UserResponse;
import com.catalogo.habilidades.model.Usuario;
import com.catalogo.habilidades.repository.UsuarioRepository;
import com.catalogo.habilidades.security.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final JwtUtil jwtUtil = new JwtUtil();
    
    /**
     * Registra um novo usuário
     */
    public AuthResponse register(RegisterRequest request) {
        // Verificar se email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.error("Email já cadastrado");
        }
        
        // Criar hash da senha
        String passwordHash = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(10));
        
        // Criar novo usuário
        Usuario usuario = new Usuario(
            request.getNome(),
            request.getEmail().toLowerCase(),
            passwordHash
        );
        
        // Salvar no banco
        usuario = usuarioRepository.save(usuario);
        
        // Gerar token JWT
        String token = jwtUtil.generateToken(
            usuario.getIdUsuario(),
            usuario.getEmail(),
            usuario.getNome()
        );
        
        // Criar resposta
        UserResponse userResponse = new UserResponse(
            usuario.getIdUsuario(),
            usuario.getNome(),
            usuario.getEmail()
        );
        
        return AuthResponse.success("Cadastro realizado com sucesso!", userResponse, token);
    }
    
    /**
     * Realiza login do usuário
     */
    public AuthResponse login(LoginRequest request) {
        // Buscar usuário por email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(
            request.getEmail().toLowerCase()
        );
        
        if (usuarioOpt.isEmpty()) {
            // Retornar erro genérico para não expor se email existe
            return AuthResponse.error("Email ou senha incorretos");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar senha
        if (!BCrypt.checkpw(request.getPassword(), usuario.getPasswordHash())) {
            return AuthResponse.error("Email ou senha incorretos");
        }
        
        // Gerar token JWT
        String token = jwtUtil.generateToken(
            usuario.getIdUsuario(),
            usuario.getEmail(),
            usuario.getNome()
        );
        
        // Criar resposta
        UserResponse userResponse = new UserResponse(
            usuario.getIdUsuario(),
            usuario.getNome(),
            usuario.getEmail()
        );
        
        return AuthResponse.success("Login realizado com sucesso!", userResponse, token);
    }
    
    /**
     * Obtém dados do usuário pelo ID
     */
    public Optional<UserResponse> getUserById(Long userId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Usuario usuario = usuarioOpt.get();
        UserResponse userResponse = new UserResponse(
            usuario.getIdUsuario(),
            usuario.getNome(),
            usuario.getEmail()
        );
        
        return Optional.of(userResponse);
    }
}

