package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.AluraCategoriaDTO;
import com.catalogo.habilidades.dto.AluraCursoDTO;
import com.catalogo.habilidades.service.AluraService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Controller para proxy das APIs da Alura
 */
@Path("/alura")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AluraController {
    
    private final AluraService aluraService;
    
    public AluraController() {
        this.aluraService = new AluraService();
    }
    
    /**
     * GET /api/alura/categorias
     * Retorna todas as categorias da Alura
     */
    @GET
    @Path("/categorias")
    public Response getCategorias() {
        try {
            List<AluraCategoriaDTO> categorias = aluraService.getCategorias();
            return Response.ok(categorias).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao buscar categorias da Alura: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /api/alura/cursos
     * Retorna todos os cursos da Alura
     */
    @GET
    @Path("/cursos")
    public Response getCursos(
            @QueryParam("categoria") String categoriaSlug,
            @QueryParam("subcategoria") String subcategoriaSlug
    ) {
        try {
            List<AluraCursoDTO> cursos;
            
            if (categoriaSlug != null || subcategoriaSlug != null) {
                cursos = aluraService.getCursosByCategoria(categoriaSlug, subcategoriaSlug);
            } else {
                cursos = aluraService.getCursos();
            }
            
            return Response.ok(cursos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao buscar cursos da Alura: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /api/alura/curso/{slug}
     * Retorna detalhes de um curso específico
     */
    @GET
    @Path("/curso/{slug}")
    public Response getCursoBySlug(@PathParam("slug") String slug) {
        try {
            AluraCursoDTO curso = aluraService.getCursoBySlug(slug);
            
            if (curso != null) {
                return Response.ok(curso).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Curso não encontrado"))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao buscar curso: " + e.getMessage()))
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

