package com.catalogo.habilidades.controller;

import com.catalogo.habilidades.dto.CursoInscricaoDTO;
import com.catalogo.habilidades.service.CursoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Controller para gerenciamento de inscrições em cursos
 */
@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoController {
    
    private final CursoService cursoService;
    
    public CursoController() {
        this.cursoService = new CursoService();
    }
    
    /**
     * POST /api/cursos/inscrever
     * Inscreve um usuário em um curso
     */
    @POST
    @Path("/inscrever")
    public Response inscreverNoCurso(CursoInscricaoDTO inscricaoDTO) {
        try {
            System.out.println("=== RECEBENDO INSCRIÇÃO ===");
            System.out.println("idUsuario: " + inscricaoDTO.getIdUsuario());
            System.out.println("cursoSlug: " + inscricaoDTO.getCursoSlug());
            System.out.println("cursoNome: " + inscricaoDTO.getCursoNome());
            System.out.println("tempoEstimado: " + inscricaoDTO.getTempoEstimado());
            System.out.println("horasEstudadas: " + inscricaoDTO.getHorasEstudadas());
            System.out.println("completado: " + inscricaoDTO.getCompletado());
            
            CursoInscricaoDTO created = cursoService.inscreverNoCurso(inscricaoDTO);
            
            System.out.println("=== INSCRIÇÃO CRIADA COM SUCESSO ===");
            System.out.println("idInscricao: " + created.getIdInscricao());
            
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            System.err.println("=== ERRO DE VALIDAÇÃO ===");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            System.err.println("=== ERRO INTERNO ===");
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao inscrever no curso: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /api/cursos/inscricoes/{usuarioId}
     * Busca todas as inscrições de um usuário
     */
    @GET
    @Path("/inscricoes/{usuarioId}")
    public Response getInscricoesByUsuario(@PathParam("usuarioId") Long usuarioId) {
        try {
            List<CursoInscricaoDTO> inscricoes = cursoService.getInscricoesByUsuario(usuarioId);
            return Response.ok(inscricoes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao buscar inscrições: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /api/cursos/inscricoes/{usuarioId}/em-progresso
     * Busca inscrições em progresso de um usuário
     */
    @GET
    @Path("/inscricoes/{usuarioId}/em-progresso")
    public Response getInscricoesEmProgresso(@PathParam("usuarioId") Long usuarioId) {
        try {
            List<CursoInscricaoDTO> inscricoes = cursoService.getInscricoesEmProgresso(usuarioId);
            return Response.ok(inscricoes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao buscar inscrições em progresso: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * PUT /api/cursos/{inscricaoId}/progresso
     * Atualiza o progresso de uma inscrição
     */
    @PUT
    @Path("/{inscricaoId}/progresso")
    public Response atualizarProgresso(
            @PathParam("inscricaoId") Long inscricaoId,
            ProgressoRequest request
    ) {
        try {
            CursoInscricaoDTO updated = cursoService.atualizarProgresso(inscricaoId, request.getHorasEstudadas());
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao atualizar progresso: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * PUT /api/cursos/{inscricaoId}/completar
     * Marca um curso como concluído e atribui XP
     */
    @PUT
    @Path("/{inscricaoId}/completar")
    public Response completarCurso(@PathParam("inscricaoId") Long inscricaoId) {
        try {
            CursoInscricaoDTO completed = cursoService.completarCurso(inscricaoId);
            return Response.ok(completed).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Erro ao completar curso: " + e.getMessage()))
                    .build();
        }
    }
    
    // Classe interna para request de atualização de progresso
    public static class ProgressoRequest {
        private Integer horasEstudadas;
        
        public ProgressoRequest() {}
        
        public Integer getHorasEstudadas() {
            return horasEstudadas;
        }
        
        public void setHorasEstudadas(Integer horasEstudadas) {
            this.horasEstudadas = horasEstudadas;
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
