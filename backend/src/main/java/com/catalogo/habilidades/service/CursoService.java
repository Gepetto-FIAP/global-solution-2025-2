package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.CursoInscricaoDTO;
import com.catalogo.habilidades.dto.DashboardStatsDTO;
import com.catalogo.habilidades.model.CursoInscricao;
import com.catalogo.habilidades.model.Habilidade;
import com.catalogo.habilidades.model.Usuario;
import com.catalogo.habilidades.repository.CursoInscricaoRepository;
import com.catalogo.habilidades.repository.HabilidadeRepository;
import com.catalogo.habilidades.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursoService {
    
    private final CursoInscricaoRepository cursoInscricaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabilidadeRepository habilidadeRepository;
    
    public CursoService() {
        this.cursoInscricaoRepository = new CursoInscricaoRepository();
        this.usuarioRepository = new UsuarioRepository();
        this.habilidadeRepository = new HabilidadeRepository();
    }
    
    /**
     * Inscrever usuário em um curso
     */
    public CursoInscricaoDTO inscreverNoCurso(CursoInscricaoDTO dto) {
        // Validações
        if (dto.getIdUsuario() == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }
        if (dto.getCursoSlug() == null || dto.getCursoSlug().trim().isEmpty()) {
            throw new IllegalArgumentException("Slug do curso é obrigatório");
        }
        if (dto.getTempoEstimado() == null || dto.getTempoEstimado() <= 0) {
            throw new IllegalArgumentException("Tempo estimado é obrigatório e deve ser maior que zero");
        }
        
        // Verificar se já existe inscrição
        if (cursoInscricaoRepository.existsByUsuarioAndCursoSlug(dto.getIdUsuario(), dto.getCursoSlug())) {
            throw new IllegalArgumentException("Usuário já está inscrito neste curso");
        }
        
        // Criar inscrição
        CursoInscricao inscricao = new CursoInscricao();
        inscricao.setIdUsuario(dto.getIdUsuario());
        inscricao.setIdHabilidade(dto.getIdHabilidade());
        inscricao.setCursoSlug(dto.getCursoSlug());
        inscricao.setCursoNome(dto.getCursoNome());
        inscricao.setTempoEstimado(dto.getTempoEstimado());
        inscricao.setHorasEstudadas(0);
        inscricao.setCompletado(false);
        
        CursoInscricao saved = cursoInscricaoRepository.save(inscricao);
        
        return convertToDTO(saved);
    }
    
    /**
     * Busca todas as inscrições de um usuário
     */
    public List<CursoInscricaoDTO> getInscricoesByUsuario(Long idUsuario) {
        List<CursoInscricao> inscricoes = cursoInscricaoRepository.findByUsuarioId(idUsuario);
        List<CursoInscricaoDTO> dtos = new ArrayList<>();
        
        for (CursoInscricao inscricao : inscricoes) {
            CursoInscricaoDTO dto = convertToDTO(inscricao);
            
            // Adicionar nome da habilidade se existir
            if (inscricao.getIdHabilidade() != null) {
                Optional<Habilidade> habilidade = habilidadeRepository.findById(inscricao.getIdHabilidade());
                habilidade.ifPresent(h -> dto.setHabilidadeNome(h.getNome()));
            }
            
            dtos.add(dto);
        }
        
        return dtos;
    }
    
    /**
     * Busca inscrições em progresso de um usuário
     */
    public List<CursoInscricaoDTO> getInscricoesEmProgresso(Long idUsuario) {
        List<CursoInscricao> inscricoes = cursoInscricaoRepository.findEmProgressoByUsuarioId(idUsuario);
        List<CursoInscricaoDTO> dtos = new ArrayList<>();
        
        for (CursoInscricao inscricao : inscricoes) {
            CursoInscricaoDTO dto = convertToDTO(inscricao);
            
            // Adicionar nome da habilidade se existir
            if (inscricao.getIdHabilidade() != null) {
                Optional<Habilidade> habilidade = habilidadeRepository.findById(inscricao.getIdHabilidade());
                habilidade.ifPresent(h -> dto.setHabilidadeNome(h.getNome()));
            }
            
            dtos.add(dto);
        }
        
        return dtos;
    }
    
    /**
     * Atualiza o progresso de uma inscrição
     */
    public CursoInscricaoDTO atualizarProgresso(Long idInscricao, Integer horasEstudadas) {
        Optional<CursoInscricao> optionalInscricao = cursoInscricaoRepository.findById(idInscricao);
        
        if (optionalInscricao.isEmpty()) {
            throw new IllegalArgumentException("Inscrição não encontrada");
        }
        
        CursoInscricao inscricao = optionalInscricao.get();
        
        if (horasEstudadas < 0) {
            throw new IllegalArgumentException("Horas estudadas não pode ser negativo");
        }
        
        inscricao.setHorasEstudadas(horasEstudadas);
        CursoInscricao updated = cursoInscricaoRepository.save(inscricao);
        
        return convertToDTO(updated);
    }
    
    /**
     * Marca um curso como concluído e atribui XP ao usuário
     */
    public CursoInscricaoDTO completarCurso(Long idInscricao) {
        Optional<CursoInscricao> optionalInscricao = cursoInscricaoRepository.findById(idInscricao);
        
        if (optionalInscricao.isEmpty()) {
            throw new IllegalArgumentException("Inscrição não encontrada");
        }
        
        CursoInscricao inscricao = optionalInscricao.get();
        
        if (inscricao.getCompletado()) {
            throw new IllegalArgumentException("Curso já está marcado como concluído");
        }
        
        // Marcar como concluído
        inscricao.setCompletado(true);
        inscricao.setDataConclusao(LocalDateTime.now());
        inscricao.setHorasEstudadas(inscricao.getTempoEstimado()); // Preencher 100%
        
        CursoInscricao updated = cursoInscricaoRepository.save(inscricao);
        
        // Adicionar XP ao usuário (1 XP por hora)
        usuarioRepository.addXp(inscricao.getIdUsuario(), inscricao.getTempoEstimado());
        
        return convertToDTO(updated);
    }
    
    /**
     * Busca estatísticas do dashboard do usuário
     */
    public DashboardStatsDTO getDashboardStats(Long idUsuario) {
        // Buscar usuário
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);
        
        if (optionalUsuario.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        
        Usuario usuario = optionalUsuario.get();
        
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setIdUsuario(usuario.getIdUsuario());
        stats.setNome(usuario.getNome());
        stats.setEmail(usuario.getEmail());
        stats.setXpTotal(usuario.getXpTotal() != null ? usuario.getXpTotal() : 0);
        
        // Contar habilidades
        List<Habilidade> habilidades = habilidadeRepository.findByUsuarioId(idUsuario);
        stats.setTotalHabilidades(habilidades.size());
        
        // Contar inscrições e cursos completos
        List<CursoInscricao> inscricoes = cursoInscricaoRepository.findByUsuarioId(idUsuario);
        stats.setTotalInscricoes(inscricoes.size());
        
        int cursosCompletos = 0;
        int horasTotais = 0;
        
        for (CursoInscricao inscricao : inscricoes) {
            if (inscricao.getCompletado()) {
                cursosCompletos++;
            }
            horasTotais += (inscricao.getHorasEstudadas() != null ? inscricao.getHorasEstudadas() : 0);
        }
        
        stats.setCursosCompletos(cursosCompletos);
        stats.setHorasTotais(horasTotais);
        
        return stats;
    }
    
    /**
     * Converte Model para DTO
     */
    private CursoInscricaoDTO convertToDTO(CursoInscricao inscricao) {
        CursoInscricaoDTO dto = new CursoInscricaoDTO();
        dto.setIdInscricao(inscricao.getIdInscricao());
        dto.setIdUsuario(inscricao.getIdUsuario());
        dto.setIdHabilidade(inscricao.getIdHabilidade());
        dto.setCursoSlug(inscricao.getCursoSlug());
        dto.setCursoNome(inscricao.getCursoNome());
        dto.setTempoEstimado(inscricao.getTempoEstimado());
        dto.setHorasEstudadas(inscricao.getHorasEstudadas());
        dto.setCompletado(inscricao.getCompletado());
        dto.setProgressoPercentual(inscricao.getProgressoPercentual());
        dto.setDataInscricao(inscricao.getDataInscricao());
        dto.setDataConclusao(inscricao.getDataConclusao());
        return dto;
    }
}
