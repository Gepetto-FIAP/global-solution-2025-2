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
        if (exception instanceof ConstraintViolationException cve) {
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
        SQLException sqlEx = null;
        if (exception instanceof SQLException sqlException) {
            sqlEx = sqlException;
        } else if (exception instanceof RuntimeException && exception.getCause() instanceof SQLException sqlCause) {
            sqlEx = sqlCause;
        }
        
        if (sqlEx != null) {
            String message = "Erro de conexão com o banco de dados";
            String sqlMessage = sqlEx.getMessage();
            
            // Mensagens específicas para diferentes tipos de erro Oracle
            if (sqlMessage != null) {
                if (sqlMessage.contains("listener") || sqlMessage.contains("ORA-12541") || 
                    sqlMessage.contains("TNS:no listener")) {
                    message = "Banco de dados não disponível. Verifique se o Oracle está rodando e acessível.";
                } else if (sqlMessage.contains("ORA-01017") || sqlMessage.contains("invalid username/password")) {
                    message = "Credenciais do banco de dados inválidas. Verifique ORACLE_USER e ORACLE_PASSWORD.";
                } else if (sqlMessage.contains("ORA-12514") || sqlMessage.contains("TNS:could not resolve")) {
                    message = "Serviço do banco de dados não encontrado. Verifique ORACLE_CONNECT_STRING.";
                } else if (sqlMessage.contains("ORA-00942") || sqlMessage.contains("table or view does not exist")) {
                    message = "Tabela não encontrada. Verifique se o script de criação do banco foi executado.";
                } else {
                    message = "Erro de conexão com o banco de dados: " + sqlMessage;
                }
            }
            
            // Log do erro completo para debug
            String sqlErrorMessage = sqlEx.getMessage();
            if (sqlErrorMessage != null) {
                System.err.println("SQL Error: " + sqlErrorMessage);
            }
            sqlEx.printStackTrace();
            
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

