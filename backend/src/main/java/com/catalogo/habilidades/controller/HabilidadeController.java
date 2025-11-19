package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.HabilidadeRequest;
import com.catalogo.habilidades.dto.HabilidadeResponse;
import com.catalogo.habilidades.security.JwtUtil;
import com.catalogo.habilidades.service.HabilidadeService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/habilidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HabilidadeController {
    
    private final HabilidadeService habilidadeService = new HabilidadeService();
    private final JwtUtil jwtUtil = new JwtUtil();
    
    /**
     * Lista todas as habilidades do usuário autenticado
     */
    @GET
    public Response listar(@Context HttpHeaders headers) {
        Long userId = getUserIdFromHeaders(headers);
        List<HabilidadeResponse> habilidades = habilidadeService.listarPorUsuario(userId);
        return Response.ok(habilidades).build();
    }
    
    /**
     * Busca uma habilidade por ID
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id, 
                                @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            HabilidadeResponse habilidade = habilidadeService.buscarPorId(id, userId);
            return Response.ok(habilidade).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Cria uma nova habilidade
     */
    @POST
    public Response criar(@Valid HabilidadeRequest request,
                          @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            HabilidadeResponse habilidade = habilidadeService.criar(request, userId);
            return Response.status(Response.Status.CREATED).entity(habilidade).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Atualiza uma habilidade
     */
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id,
                             @Valid HabilidadeRequest request,
                             @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            HabilidadeResponse habilidade = habilidadeService.atualizar(id, request, userId);
            return Response.ok(habilidade).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Deleta uma habilidade
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id,
                           @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            habilidadeService.deletar(id, userId);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Lista cursos sugeridos para uma habilidade
     */
    @GET
    @Path("/{id}/cursos")
    public Response listarCursosSugeridos(@PathParam("id") Long idHabilidade,
                                          @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            List<com.catalogo.habilidades.dto.CursoResponse> cursos = 
                new com.catalogo.habilidades.service.CursoService()
                    .buscarSugeridosPorHabilidade(idHabilidade, userId);
            return Response.ok(cursos).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
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

