package com.catalogo.habilidades.dto;

public class HabilidadeResponse {
    
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private String nivel;
    private Integer progressoPercentual;
    
    public HabilidadeResponse() {
    }
    
    public HabilidadeResponse(Long id, String nome, String categoria, String descricao, 
                              String nivel, Integer progressoPercentual) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.nivel = nivel;
        this.progressoPercentual = progressoPercentual;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Integer getProgressoPercentual() {
        return progressoPercentual;
    }
    
    public void setProgressoPercentual(Integer progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }
}

