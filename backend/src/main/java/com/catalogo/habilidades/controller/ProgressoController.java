package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.ProgressoCursoRequest;
import com.catalogo.habilidades.model.UsuarioCurso;
import com.catalogo.habilidades.security.JwtUtil;
import com.catalogo.habilidades.service.ProgressoService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/progresso")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProgressoController {
    
    private final ProgressoService progressoService = new ProgressoService();
    private final JwtUtil jwtUtil = new JwtUtil();
    
    /**
     * Atualiza progresso de um curso
     */
    @PUT
    @Path("/curso/{id}")
    public Response atualizarProgresso(@PathParam("id") Long idCurso,
                                      @Valid ProgressoCursoRequest request,
                                      @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            UsuarioCurso usuarioCurso = progressoService.atualizarProgresso(userId, idCurso, request);
            return Response.ok(usuarioCurso).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Atualiza progresso de um curso da Alura
     */
    @PUT
    @Path("/curso-alura/{idAlura}")
    public Response atualizarProgressoAlura(@PathParam("idAlura") String idCursoAlura,
                                           @Valid ProgressoCursoRequest request,
                                           @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            UsuarioCurso usuarioCurso = progressoService.atualizarProgressoAlura(userId, idCursoAlura, request);
            return Response.ok(usuarioCurso).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Marca curso como concluído
     */
    @POST
    @Path("/curso/{id}/concluir")
    public Response marcarComoConcluido(@PathParam("id") Long idCurso,
                                         @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            UsuarioCurso usuarioCurso = progressoService.marcarComoConcluido(userId, idCurso);
            return Response.ok(usuarioCurso).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Marca curso da Alura como concluído
     */
    @POST
    @Path("/curso-alura/{idAlura}/concluir")
    public Response marcarComoConcluidoAlura(@PathParam("idAlura") String idCursoAlura,
                                             @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            UsuarioCurso usuarioCurso = progressoService.marcarComoConcluidoAlura(userId, idCursoAlura);
            return Response.ok(usuarioCurso).build();
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

