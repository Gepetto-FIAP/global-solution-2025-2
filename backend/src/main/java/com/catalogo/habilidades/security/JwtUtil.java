package com.catalogo.habilidades.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret:seu-secret-super-secreto-mude-isso-em-producao}")
    private String secret;
    
    @Value("${jwt.expiration:604800000}") // 7 dias em milissegundos
    private Long expiration;
    
    /**
     * Gera uma chave secreta a partir da string de secret
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Gera um token JWT para o usuário
     */
    public String generateToken(Long userId, String email, String nome) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("nome", nome);
        
        return createToken(claims);
    }
    
    /**
     * Cria o token JWT com os claims fornecidos
     */
    private String createToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * Extrai o userId do token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = extractAllClaims(token);
        Object userId = claims.get("userId");
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }
    
    /**
     * Extrai o email do token
     */
    public String getEmailFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("email");
    }
    
    /**
     * Extrai o nome do token
     */
    public String getNomeFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("nome");
    }
    
    /**
     * Extrai todas as claims do token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair claims do token", e);
        }
    }
    
    /**
     * Verifica se o token é válido
     */
    public Boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

