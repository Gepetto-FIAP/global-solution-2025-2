package com.catalogo.habilidades.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CursoManualRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
    private String nome;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    private String categoria;
    
    @Size(max = 500, message = "Link deve ter no máximo 500 caracteres")
    private String link;
    
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String descricao;
    
    private Integer duracaoHoras;
    
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
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Integer getDuracaoHoras() {
        return duracaoHoras;
    }
    
    public void setDuracaoHoras(Integer duracaoHoras) {
        this.duracaoHoras = duracaoHoras;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}

