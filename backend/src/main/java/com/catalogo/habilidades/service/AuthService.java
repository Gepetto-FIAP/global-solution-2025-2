package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.AuthResponse;
import com.catalogo.habilidades.dto.LoginRequest;
import com.catalogo.habilidades.dto.RegisterRequest;
import com.catalogo.habilidades.dto.UserResponse;
import com.catalogo.habilidades.model.Usuario;
import com.catalogo.habilidades.repository.UsuarioRepository;
import com.catalogo.habilidades.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    
    /**
     * Registra um novo usuário
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar se email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.error("Email já cadastrado");
        }
        
        // Criar hash da senha
        String passwordHash = passwordEncoder.encode(request.getPassword());
        
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
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
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

