package com.catalogo.habilidades.model;

import java.time.LocalDateTime;

public class Habilidade {
    
    private Long idHabilidade;
    private Long idUsuario;
    private String nome;
    private String categoria;
    private String descricao;
    private String nivel; // 'Iniciante', 'Intermediário', 'Avançado'
    private Integer progressoPercentual; // 0-100
    
    // Construtores
    public Habilidade() {
        this.progressoPercentual = 0;
    }
    
    public Habilidade(Long idUsuario, String nome, String categoria, String descricao, String nivel) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.nivel = nivel;
        this.progressoPercentual = 0;
    }
    
    // Getters e Setters
    public Long getIdHabilidade() {
        return idHabilidade;
    }
    
    public void setIdHabilidade(Long idHabilidade) {
        this.idHabilidade = idHabilidade;
    }
    
    public Long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

