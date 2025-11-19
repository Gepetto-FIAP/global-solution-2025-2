package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.ProgressoCursoRequest;
import com.catalogo.habilidades.model.Curso;
import com.catalogo.habilidades.model.UsuarioCurso;
import com.catalogo.habilidades.repository.CursoRepository;
import com.catalogo.habilidades.repository.UsuarioCursoRepository;

import java.time.LocalDateTime;

public class ProgressoService {
    
    private static final int XP_POR_HORA = 10;
    
    private final UsuarioCursoRepository usuarioCursoRepository = new UsuarioCursoRepository();
    private final CursoRepository cursoRepository = new CursoRepository();
    
    /**
     * Atualiza progresso de um curso
     */
    public UsuarioCurso atualizarProgresso(Long idUsuario, Long idCurso, ProgressoCursoRequest request) {
        // Buscar curso para calcular XP
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        
        // Buscar ou criar relacionamento usuário-curso
        UsuarioCurso usuarioCurso = usuarioCursoRepository
                .findByUsuarioIdAndCursoId(idUsuario, idCurso)
                .orElse(new UsuarioCurso(idUsuario, idCurso));
        
        // Atualizar progresso
        if (request.getProgressoPercentual() != null) {
            usuarioCurso.setProgressoPercentual(request.getProgressoPercentual());
        }
        
        // Se marcou como concluído
        if (request.getConcluido() != null && request.getConcluido()) {
            usuarioCurso.setConcluido(true);
            usuarioCurso.setProgressoPercentual(100);
            usuarioCurso.setDataConclusao(LocalDateTime.now());
            
            // Calcular XP ganho (10 XP por hora)
            if (curso.getDuracaoHoras() != null && curso.getDuracaoHoras() > 0) {
                usuarioCurso.setXpGanho(curso.getDuracaoHoras() * XP_POR_HORA);
            }
        } else if (request.getConcluido() != null && !request.getConcluido()) {
            usuarioCurso.setConcluido(false);
            usuarioCurso.setDataConclusao(null);
            usuarioCurso.setXpGanho(0);
        }
        
        return usuarioCursoRepository.save(usuarioCurso);
    }
    
    /**
     * Atualiza progresso de um curso da Alura
     */
    public UsuarioCurso atualizarProgressoAlura(Long idUsuario, String idCursoAlura, ProgressoCursoRequest request) {
        // Buscar ou criar relacionamento usuário-curso Alura
        UsuarioCurso usuarioCurso = usuarioCursoRepository
                .findByUsuarioIdAndCursoAluraId(idUsuario, idCursoAlura)
                .orElse(new UsuarioCurso(idUsuario, idCursoAlura));
        
        // Atualizar progresso
        if (request.getProgressoPercentual() != null) {
            usuarioCurso.setProgressoPercentual(request.getProgressoPercentual());
        }
        
        // Se marcou como concluído
        if (request.getConcluido() != null && request.getConcluido()) {
            usuarioCurso.setConcluido(true);
            usuarioCurso.setProgressoPercentual(100);
            usuarioCurso.setDataConclusao(LocalDateTime.now());
            
            // Buscar curso da Alura para calcular XP
            Curso curso = cursoRepository.findByIdAlura(idCursoAlura).orElse(null);
            if (curso != null && curso.getDuracaoHoras() != null && curso.getDuracaoHoras() > 0) {
                usuarioCurso.setXpGanho(curso.getDuracaoHoras() * XP_POR_HORA);
            }
        } else if (request.getConcluido() != null && !request.getConcluido()) {
            usuarioCurso.setConcluido(false);
            usuarioCurso.setDataConclusao(null);
            usuarioCurso.setXpGanho(0);
        }
        
        return usuarioCursoRepository.save(usuarioCurso);
    }
    
    /**
     * Marca curso como concluído
     */
    public UsuarioCurso marcarComoConcluido(Long idUsuario, Long idCurso) {
        ProgressoCursoRequest request = new ProgressoCursoRequest();
        request.setProgressoPercentual(100);
        request.setConcluido(true);
        return atualizarProgresso(idUsuario, idCurso, request);
    }
    
    /**
     * Marca curso da Alura como concluído
     */
    public UsuarioCurso marcarComoConcluidoAlura(Long idUsuario, String idCursoAlura) {
        ProgressoCursoRequest request = new ProgressoCursoRequest();
        request.setProgressoPercentual(100);
        request.setConcluido(true);
        return atualizarProgressoAlura(idUsuario, idCursoAlura, request);
    }
}

