package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.AuthResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.sql.SQLException;
import java.util.Set;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        // Tratar exceções de validação
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
            
            if (!violations.isEmpty()) {
                // Retornar apenas o primeiro erro para manter compatibilidade com o contrato
                String firstError = violations.iterator().next().getMessage();
                return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(AuthResponse.error(firstError))
                    .build();
            }
        }
        
        // Tratar erros de SQL (conexão com banco)
        Throwable cause = exception.getCause();
        if (exception instanceof RuntimeException && cause instanceof SQLException) {
            SQLException sqlEx = (SQLException) cause;
            String message = "Erro de conexão com o banco de dados";
            if (sqlEx.getMessage().contains("listener") || sqlEx.getMessage().contains("ORA-12541")) {
                message = "Banco de dados não disponível. Verifique a conexão.";
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(AuthResponse.error(message))
                .build();
        }
        
        // Tratar exceções genéricas
        String errorMessage = "Erro interno do servidor";
        if (exception.getMessage() != null && !exception.getMessage().isEmpty()) {
            errorMessage = exception.getMessage();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.APPLICATION_JSON)
            .entity(AuthResponse.error(errorMessage))
            .build();
    }
}

