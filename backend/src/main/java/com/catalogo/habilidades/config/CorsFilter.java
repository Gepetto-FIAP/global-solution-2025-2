package com.catalogo.habilidades.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filtro CORS para permitir requisições do frontend
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        // Obter a origem da requisição
        String origin = requestContext.getHeaderString("Origin");
        
        // Lista de origens permitidas
        String[] allowedOrigins = {
            "http://localhost:3000",
            "https://global-solution-2025-2.vercel.app",
            "https://global-solution-2025-2-git-feat-luiz-lgpgomes-projects.vercel.app/auth/login"
        };
        
        // Verificar se a origem está na lista de permitidas
        if (origin != null) {
            for (String allowedOrigin : allowedOrigins) {
                if (origin.equals(allowedOrigin)) {
                    responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }
        
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", 
                "origin, content-type, accept, authorization");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", 
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}

