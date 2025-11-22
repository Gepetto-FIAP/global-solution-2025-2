package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.DashboardStatsDTO;
import com.catalogo.habilidades.service.CursoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controller para estatísticas do dashboard
 */
@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardController {
    
    private final CursoService cursoService;
    
    public DashboardController() {
        this.cursoService = new CursoService();
    }
    
    /**
     * GET /api/dashboard/stats/{usuarioId}
     * Retorna estatísticas do dashboard do usuário
     */
    @GET
    @Path("/stats/{usuarioId}")
    public Response getDashboardStats(@PathParam("usuarioId") Long usuarioId) {
        try {
            DashboardStatsDTO stats = cursoService.getDashboardStats(usuarioId);
            return Response.ok(stats).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao buscar estatísticas: " + e.getMessage()))
                    .build();
        }
    }
    
    // Classe interna para respostas de erro
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}

