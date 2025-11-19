package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.CursoManualRequest;
import com.catalogo.habilidades.dto.CursoResponse;
import com.catalogo.habilidades.security.JwtUtil;
import com.catalogo.habilidades.service.CursoService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoController {
    
    private final CursoService cursoService = new CursoService();
    private final JwtUtil jwtUtil = new JwtUtil();
    
    /**
     * Busca curso por ID
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id,
                                 @Context HttpHeaders headers) {
        try {
            Long userId = getUserIdFromHeaders(headers);
            CursoResponse curso = cursoService.buscarPorId(id, userId);
            return Response.ok(curso).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Cria um curso manual
     */
    @POST
    @Path("/manual")
    public Response criarCursoManual(@Valid CursoManualRequest request) {
        try {
            CursoResponse curso = cursoService.criarCursoManual(request);
            return Response.status(Response.Status.CREATED).entity(curso).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
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
            return null; // Permitir acesso sem autenticação para alguns endpoints
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return null;
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}

