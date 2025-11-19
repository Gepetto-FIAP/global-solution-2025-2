package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.DashboardStatsResponse;
import com.catalogo.habilidades.security.JwtUtil;
import com.catalogo.habilidades.service.DashboardService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardController {
    
    private final DashboardService dashboardService = new DashboardService();
    private final JwtUtil jwtUtil = new JwtUtil();
    
    /**
     * Obtém estatísticas do dashboard
     */
    @GET
    @Path("/stats")
    public Response obterEstatisticas(@Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            DashboardStatsResponse stats = dashboardService.obterEstatisticas(userId);
            return Response.ok(stats).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Extrai userId dos headers
     */
    private Long getUserIdFromHeaders(HttpHeaders headers) {
        String authHeader = headers.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token não fornecido");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}

