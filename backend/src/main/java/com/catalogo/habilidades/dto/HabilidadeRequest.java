package com.catalogo.habilidades.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class HabilidadeRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    private String categoria;
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String descricao;
    
    private String nivel; // 'Iniciante', 'Intermediário', 'Avançado'
    
    // Getters e Setters
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}

