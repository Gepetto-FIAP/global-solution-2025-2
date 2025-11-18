package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.AuthResponse;
import com.catalogo.habilidades.dto.LoginRequest;
import com.catalogo.habilidades.dto.RegisterRequest;
import com.catalogo.habilidades.dto.UserResponse;
import com.catalogo.habilidades.security.JwtUtil;
import com.catalogo.habilidades.service.AuthService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    
    private final AuthService authService = new AuthService();
    private final JwtUtil jwtUtil = new JwtUtil();
    
    /**
     * Registra um novo usuário
     */
    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        AuthResponse response = authService.register(request);
        
        if (response.isSuccess()) {
            return Response.status(Response.Status.CREATED).entity(response).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
    
    /**
     * Realiza login do usuário
     */
    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        AuthResponse response = authService.login(request);
        
        if (response.isSuccess()) {
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }
    }
    
    /**
     * Realiza logout (opcional)
     */
    @POST
    @Path("/logout")
    public Response logout() {
        // Logout pode ser feito apenas removendo o token no frontend
        // Este endpoint é opcional e pode invalidar o token no backend se necessário
        return Response.ok(AuthResponse.success("Logout realizado com sucesso", null, null)).build();
    }
    
    /**
     * Retorna dados do usuário autenticado atual
     */
    @GET
    @Path("/me")
    public Response getCurrentUser(@Context HttpHeaders headers) {
        // Extrair token do header
        String authHeader = headers.getHeaderString("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(AuthResponse.error("Token não fornecido"))
                .build();
        }
        
        String token = authHeader.substring(7);
        
        // Validar token
        if (!jwtUtil.validateToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(AuthResponse.error("Token inválido ou expirado"))
                .build();
        }
        
        // Extrair userId do token
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        // Buscar usuário
        return authService.getUserById(userId)
            .map(user -> {
                AuthResponse response = new AuthResponse();
                response.setSuccess(true);
                response.setUser(user);
                return Response.ok(response).build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND)
                .entity(AuthResponse.error("Usuário não encontrado"))
                .build());
    }
}

