package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.AuthResponse;
import com.catalogo.habilidades.dto.LoginRequest;
import com.catalogo.habilidades.dto.RegisterRequest;
import com.catalogo.habilidades.dto.UserResponse;
import com.catalogo.habilidades.security.JwtUtil;
import com.catalogo.habilidades.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Registra um novo usuário
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Realiza login do usuário
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * Realiza logout (opcional)
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        // Logout pode ser feito apenas removendo o token no frontend
        // Este endpoint é opcional e pode invalidar o token no backend se necessário
        return ResponseEntity.ok(AuthResponse.success("Logout realizado com sucesso", null, null));
    }
    
    /**
     * Retorna dados do usuário autenticado atual
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // Extrair token do header
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.error("Token não fornecido"));
        }
        
        String token = authHeader.substring(7);
        
        // Validar token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.error("Token inválido ou expirado"));
        }
        
        // Extrair userId do token
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        // Buscar usuário
        return authService.getUserById(userId)
            .map(user -> {
                AuthResponse response = new AuthResponse();
                response.setSuccess(true);
                response.setUser(user);
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(AuthResponse.error("Usuário não encontrado")));
    }
}

