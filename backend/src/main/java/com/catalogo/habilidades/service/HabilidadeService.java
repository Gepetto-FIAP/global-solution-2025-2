package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.HabilidadeDTO;
import com.catalogo.habilidades.model.Habilidade;
import com.catalogo.habilidades.model.CursoInscricao;
import com.catalogo.habilidades.repository.HabilidadeRepository;
import com.catalogo.habilidades.repository.CursoInscricaoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabilidadeService {
    
    private final HabilidadeRepository habilidadeRepository;
    private final CursoInscricaoRepository cursoInscricaoRepository;
    
    public HabilidadeService() {
        this.habilidadeRepository = new HabilidadeRepository();
        this.cursoInscricaoRepository = new CursoInscricaoRepository();
    }
    
    /**
     * Busca habilidades de um usuário
     */
    public List<HabilidadeDTO> getHabilidadesByUsuario(Long idUsuario) {
        List<Habilidade> habilidades = habilidadeRepository.findByUsuarioId(idUsuario);
        List<HabilidadeDTO> habilidadeDTOs = new ArrayList<>();
        
        for (Habilidade habilidade : habilidades) {
            HabilidadeDTO dto = convertToDTO(habilidade);
            
            // Contar cursos associados à habilidade
            List<CursoInscricao> inscricoes = cursoInscricaoRepository.findByUsuarioId(idUsuario);
            int totalCursos = 0;
            int cursosCompletos = 0;
            
            for (CursoInscricao inscricao : inscricoes) {
                if (inscricao.getIdHabilidade() != null && inscricao.getIdHabilidade().equals(habilidade.getIdHabilidade())) {
                    totalCursos++;
                    if (inscricao.getCompletado()) {
                        cursosCompletos++;
                    }
                }
            }
            
            dto.setTotalCursos(totalCursos);
            dto.setCursosCompletos(cursosCompletos);
            
            habilidadeDTOs.add(dto);
        }
        
        return habilidadeDTOs;
    }
    
    /**
     * Cria uma nova habilidade
     */
    public HabilidadeDTO criarHabilidade(HabilidadeDTO dto) {
        // Validações
        if (dto.getIdUsuario() == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da habilidade é obrigatório");
        }
        if (dto.getCategoriaSlug() == null || dto.getCategoriaSlug().trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        
        // Converter DTO para Model
        Habilidade habilidade = new Habilidade();
        habilidade.setIdUsuario(dto.getIdUsuario());
        habilidade.setNome(dto.getNome());
        habilidade.setCategoriaSlug(dto.getCategoriaSlug());
        habilidade.setSubcategoriaSlug(dto.getSubcategoriaSlug());
        habilidade.setNivel(dto.getNivel() != null ? dto.getNivel() : "Iniciante");
        
        // Salvar no banco
        Habilidade saved = habilidadeRepository.save(habilidade);
        
        return convertToDTO(saved);
    }
    
    /**
     * Atualiza uma habilidade existente
     */
    public HabilidadeDTO atualizarHabilidade(Long idHabilidade, HabilidadeDTO dto) {
        Optional<Habilidade> optionalHabilidade = habilidadeRepository.findById(idHabilidade);
        
        if (optionalHabilidade.isEmpty()) {
            throw new IllegalArgumentException("Habilidade não encontrada");
        }
        
        Habilidade habilidade = optionalHabilidade.get();
        
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            habilidade.setNome(dto.getNome());
        }
        if (dto.getCategoriaSlug() != null) {
            habilidade.setCategoriaSlug(dto.getCategoriaSlug());
        }
        if (dto.getSubcategoriaSlug() != null) {
            habilidade.setSubcategoriaSlug(dto.getSubcategoriaSlug());
        }
        if (dto.getNivel() != null) {
            habilidade.setNivel(dto.getNivel());
        }
        
        Habilidade updated = habilidadeRepository.save(habilidade);
        
        return convertToDTO(updated);
    }
    
    /**
     * Deleta uma habilidade
     */
    public void deletarHabilidade(Long idHabilidade) {
        habilidadeRepository.delete(idHabilidade);
    }
    
    /**
     * Converte Model para DTO
     */
    private HabilidadeDTO convertToDTO(Habilidade habilidade) {
        HabilidadeDTO dto = new HabilidadeDTO();
        dto.setIdHabilidade(habilidade.getIdHabilidade());
        dto.setIdUsuario(habilidade.getIdUsuario());
        dto.setNome(habilidade.getNome());
        dto.setCategoriaSlug(habilidade.getCategoriaSlug());
        dto.setSubcategoriaSlug(habilidade.getSubcategoriaSlug());
        dto.setNivel(habilidade.getNivel());
        dto.setDataCriacao(habilidade.getDataCriacao());
        return dto;
    }
}

