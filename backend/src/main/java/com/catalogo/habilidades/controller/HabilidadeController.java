package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.HabilidadeDTO;
import com.catalogo.habilidades.service.HabilidadeService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Controller para gerenciamento de habilidades
 */
@Path("/habilidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HabilidadeController {
    
    private final HabilidadeService habilidadeService;
    
    public HabilidadeController() {
        this.habilidadeService = new HabilidadeService();
    }
    
    /**
     * GET /api/habilidades/usuario/{id}
     * Busca todas as habilidades de um usuário
     */
    @GET
    @Path("/usuario/{id}")
    public Response getHabilidadesByUsuario(@PathParam("id") Long idUsuario) {
        try {
            List<HabilidadeDTO> habilidades = habilidadeService.getHabilidadesByUsuario(idUsuario);
            return Response.ok(habilidades).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao buscar habilidades: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /api/habilidades
     * Cria uma nova habilidade
     */
    @POST
    public Response criarHabilidade(HabilidadeDTO habilidadeDTO) {
        try {
            HabilidadeDTO created = habilidadeService.criarHabilidade(habilidadeDTO);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao criar habilidade: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * PUT /api/habilidades/{id}
     * Atualiza uma habilidade existente
     */
    @PUT
    @Path("/{id}")
    public Response atualizarHabilidade(@PathParam("id") Long id, HabilidadeDTO habilidadeDTO) {
        try {
            HabilidadeDTO updated = habilidadeService.atualizarHabilidade(id, habilidadeDTO);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao atualizar habilidade: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * DELETE /api/habilidades/{id}
     * Deleta uma habilidade
     */
    @DELETE
    @Path("/{id}")
    public Response deletarHabilidade(@PathParam("id") Long id) {
        try {
            habilidadeService.deletarHabilidade(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao deletar habilidade: " + e.getMessage()))
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

