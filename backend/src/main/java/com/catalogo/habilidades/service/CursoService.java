package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.CursoManualRequest;
import com.catalogo.habilidades.dto.CursoResponse;
import com.catalogo.habilidades.model.Curso;
import com.catalogo.habilidades.model.UsuarioCurso;
import com.catalogo.habilidades.repository.CursoRepository;
import com.catalogo.habilidades.repository.UsuarioCursoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CursoService {
    
    private final CursoRepository cursoRepository = new CursoRepository();
    private final UsuarioCursoRepository usuarioCursoRepository = new UsuarioCursoRepository();
    private final AluraApiService aluraApiService = new AluraApiService();
    
    /**
     * Busca cursos sugeridos para uma habilidade
     */
    public List<CursoResponse> buscarSugeridosPorHabilidade(Long idHabilidade, Long idUsuario) {
        // Buscar habilidade para obter categoria e nome
        com.catalogo.habilidades.repository.HabilidadeRepository habilidadeRepo = 
            new com.catalogo.habilidades.repository.HabilidadeRepository();
        com.catalogo.habilidades.model.Habilidade habilidade = habilidadeRepo.findById(idHabilidade)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada"));
        
        if (!habilidade.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("Habilidade não pertence ao usuário");
        }
        
        // Buscar cursos da Alura por palavras-chave do nome da habilidade
        List<Curso> cursosAlura = aluraApiService.buscarCursosPorPalavrasChave(habilidade.getNome());
        
        // Buscar também cursos locais por categoria
        List<Curso> cursosLocais = cursoRepository.findByCategoria(habilidade.getCategoria());
        
        // Combinar e remover duplicatas (por ID Alura)
        List<Curso> todosCursos = cursosAlura;
        for (Curso cursoLocal : cursosLocais) {
            boolean jaExiste = todosCursos.stream()
                    .anyMatch(c -> c.getIdAlura() != null && c.getIdAlura().equals(cursoLocal.getIdAlura()));
            if (!jaExiste) {
                todosCursos.add(cursoLocal);
            }
        }
        
        // Mapear para response incluindo status do usuário
        return todosCursos.stream()
                .map(curso -> mapToResponse(curso, idUsuario))
                .collect(Collectors.toList());
    }
    
    /**
     * Cria um curso manual
     */
    public CursoResponse criarCursoManual(CursoManualRequest request) {
        Curso curso = new Curso();
        curso.setNome(request.getNome());
        curso.setCategoria(request.getCategoria());
        curso.setLink(request.getLink());
        curso.setDescricao(request.getDescricao());
        curso.setDuracaoHoras(request.getDuracaoHoras());
        curso.setNivel(request.getNivel());
        curso.setOrigem("MANUAL");
        
        curso = cursoRepository.save(curso);
        return mapToResponse(curso, null);
    }
    
    /**
     * Busca curso por ID
     */
    public CursoResponse buscarPorId(Long idCurso, Long idUsuario) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        
        return mapToResponse(curso, idUsuario);
    }
    
    /**
     * Mapeia Curso para CursoResponse incluindo status do usuário
     */
    private CursoResponse mapToResponse(Curso curso, Long idUsuario) {
        CursoResponse response = new CursoResponse();
        response.setId(curso.getIdCurso());
        response.setIdAlura(curso.getIdAlura());
        response.setNome(curso.getNome());
        response.setCategoria(curso.getCategoria());
        response.setLink(curso.getLink());
        response.setDescricao(curso.getDescricao());
        response.setDuracaoHoras(curso.getDuracaoHoras());
        response.setNivel(curso.getNivel());
        response.setInstrutor(curso.getInstrutor());
        response.setImagemUrl(curso.getImagemUrl());
        response.setOrigem(curso.getOrigem());
        
        // Buscar status do usuário se fornecido
        if (idUsuario != null) {
            if (curso.getIdCurso() != null) {
                usuarioCursoRepository.findByUsuarioIdAndCursoId(idUsuario, curso.getIdCurso())
                        .ifPresent(uc -> {
                            response.setConcluido(uc.getConcluido());
                            response.setProgressoPercentual(uc.getProgressoPercentual());
                        });
            } else if (curso.getIdAlura() != null) {
                usuarioCursoRepository.findByUsuarioIdAndCursoAluraId(idUsuario, curso.getIdAlura())
                        .ifPresent(uc -> {
                            response.setConcluido(uc.getConcluido());
                            response.setProgressoPercentual(uc.getProgressoPercentual());
                        });
            }
        }
        
        return response;
    }
}

