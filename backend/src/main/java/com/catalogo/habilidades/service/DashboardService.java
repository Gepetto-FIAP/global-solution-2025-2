package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.DashboardStatsResponse;
import com.catalogo.habilidades.model.Usuario;
import com.catalogo.habilidades.repository.HabilidadeRepository;
import com.catalogo.habilidades.repository.MetaMensalRepository;
import com.catalogo.habilidades.repository.UsuarioCursoRepository;
import com.catalogo.habilidades.repository.UsuarioRepository;

import java.time.LocalDate;

public class DashboardService {
    
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final HabilidadeRepository habilidadeRepository = new HabilidadeRepository();
    private final UsuarioCursoRepository usuarioCursoRepository = new UsuarioCursoRepository();
    private final MetaMensalRepository metaMensalRepository = new MetaMensalRepository();
    
    /**
     * Obtém estatísticas do dashboard para um usuário
     */
    public DashboardStatsResponse obterEstatisticas(Long idUsuario) {
        // Buscar usuário
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Contar habilidades
        int habilidadesAdquiridas = habilidadeRepository.findByUsuarioId(idUsuario).size();
        
        // Contar cursos concluídos (total)
        int cursosConcluidos = usuarioCursoRepository.findConcluidosByUsuarioId(idUsuario).size();
        
        // Obter XP total do usuário
        int xpTotal = usuario.getXpTotal() != null ? usuario.getXpTotal() : 0;
        
        // Obter meta mensal
        LocalDate now = LocalDate.now();
        int mes = now.getMonthValue();
        int ano = now.getYear();
        
        var metaMensal = metaMensalRepository.findOrCreateCurrentMonth(idUsuario);
        int metaCursosMensal = metaMensal.getMetaCursos();
        int cursosConcluidosMes = metaMensal.getCursosConcluidos();
        
        return new DashboardStatsResponse(
                habilidadesAdquiridas,
                cursosConcluidos,
                xpTotal,
                metaCursosMensal,
                cursosConcluidosMes
        );
    }
}

