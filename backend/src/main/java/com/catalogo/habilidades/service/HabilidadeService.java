package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.HabilidadeRequest;
import com.catalogo.habilidades.dto.HabilidadeResponse;
import com.catalogo.habilidades.model.Habilidade;
import com.catalogo.habilidades.repository.HabilidadeRepository;

import java.util.List;
import java.util.stream.Collectors;

public class HabilidadeService {
    
    private final HabilidadeRepository habilidadeRepository = new HabilidadeRepository();
    
    /**
     * Lista todas as habilidades de um usuário
     */
    public List<HabilidadeResponse> listarPorUsuario(Long idUsuario) {
        List<Habilidade> habilidades = habilidadeRepository.findByUsuarioId(idUsuario);
        return habilidades.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca uma habilidade por ID
     */
    public HabilidadeResponse buscarPorId(Long idHabilidade, Long idUsuario) {
        Habilidade habilidade = habilidadeRepository.findById(idHabilidade)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada"));
        
        if (!habilidade.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("Habilidade não pertence ao usuário");
        }
        
        return mapToResponse(habilidade);
    }
    
    /**
     * Cria uma nova habilidade
     */
    public HabilidadeResponse criar(HabilidadeRequest request, Long idUsuario) {
        Habilidade habilidade = new Habilidade();
        habilidade.setIdUsuario(idUsuario);
        habilidade.setNome(request.getNome());
        habilidade.setCategoria(request.getCategoria());
        habilidade.setDescricao(request.getDescricao());
        habilidade.setNivel(request.getNivel());
        habilidade.setProgressoPercentual(0);
        
        habilidade = habilidadeRepository.save(habilidade);
        return mapToResponse(habilidade);
    }
    
    /**
     * Atualiza uma habilidade
     */
    public HabilidadeResponse atualizar(Long idHabilidade, HabilidadeRequest request, Long idUsuario) {
        Habilidade habilidade = habilidadeRepository.findById(idHabilidade)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada"));
        
        if (!habilidade.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("Habilidade não pertence ao usuário");
        }
        
        habilidade.setNome(request.getNome());
        habilidade.setCategoria(request.getCategoria());
        habilidade.setDescricao(request.getDescricao());
        habilidade.setNivel(request.getNivel());
        
        habilidade = habilidadeRepository.update(habilidade);
        return mapToResponse(habilidade);
    }
    
    /**
     * Deleta uma habilidade
     */
    public void deletar(Long idHabilidade, Long idUsuario) {
        if (!habilidadeRepository.belongsToUsuario(idHabilidade, idUsuario)) {
            throw new RuntimeException("Habilidade não encontrada ou não pertence ao usuário");
        }
        
        habilidadeRepository.delete(idHabilidade, idUsuario);
    }
    
    /**
     * Mapeia Habilidade para HabilidadeResponse
     */
    private HabilidadeResponse mapToResponse(Habilidade habilidade) {
        return new HabilidadeResponse(
                habilidade.getIdHabilidade(),
                habilidade.getNome(),
                habilidade.getCategoria(),
                habilidade.getDescricao(),
                habilidade.getNivel(),
                habilidade.getProgressoPercentual()
        );
    }
}

