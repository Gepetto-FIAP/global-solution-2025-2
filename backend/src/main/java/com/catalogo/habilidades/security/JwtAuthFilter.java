package com.catalogo.habilidades.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class JwtAuthFilter implements ContainerRequestFilter {
    
    private final JwtUtil jwtUtil = new JwtUtil();
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Ignorar endpoints públicos
        String path = requestContext.getUriInfo().getPath();
        if (path.startsWith("auth/login") || path.startsWith("auth/register")) {
            return;
        }
        
        // Extrair token do header
        String authHeader = requestContext.getHeaderString("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"success\":false,\"message\":\"Token não fornecido\"}")
                    .build()
            );
            return;
        }
        
        String token = authHeader.substring(7);
        
        // Validar token
        if (!jwtUtil.validateToken(token)) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"success\":false,\"message\":\"Token inválido ou expirado\"}")
                    .build()
            );
            return;
        }
        
        // Adicionar userId ao contexto da requisição
        Long userId = jwtUtil.getUserIdFromToken(token);
        requestContext.setProperty("userId", userId);
    }
}

