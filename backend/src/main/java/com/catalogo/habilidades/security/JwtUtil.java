package com.catalogo.habilidades.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    
    private final String secret;
    private final long expiration;
    private final SecretKey key;
    
    public JwtUtil() {
        // Obter secret e expiration das variáveis de ambiente ou usar valores padrão
        String secretEnv = System.getenv("JWT_SECRET");
        if (secretEnv == null || secretEnv.isEmpty()) {
            secret = "seu-secret-super-secreto-mude-isso-em-producao";
        } else {
            secret = secretEnv;
        }
        
        String expirationStr = System.getenv("JWT_EXPIRATION");
        if (expirationStr != null && !expirationStr.isEmpty()) {
            expiration = Long.parseLong(expirationStr);
        } else {
            expiration = 604800000L; // 7 dias em milissegundos
        }
        
        // Criar chave secreta
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Gera um token JWT para o usuário
     */
    public String generateToken(Long userId, String email, String nome) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("nome", nome)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }
    
    /**
     * Valida um token JWT
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrai o ID do usuário do token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return Long.parseLong(claims.getSubject());
    }
    
    /**
     * Extrai o email do token
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.get("email", String.class);
    }
}

