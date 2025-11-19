package com.catalogo.habilidades.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filtro CORS para permitir requisições do frontend
 */
@Provider
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Tratar requisições OPTIONS (preflight)
        if ("OPTIONS".equals(requestContext.getMethod())) {
            requestContext.abortWith(
                Response.status(Response.Status.OK)
                    .header("Access-Control-Allow-Origin", "http://localhost:3000")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Headers", 
                        "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Methods", 
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .build()
            );
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        // Usar putSingle() ao invés de add() para evitar valores duplicados
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "http://localhost:3000");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", 
                "origin, content-type, accept, authorization");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", 
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}

