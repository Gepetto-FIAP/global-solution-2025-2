package com.catalogo.habilidades.model;

import java.time.LocalDateTime;

public class Habilidade {
    
    private Long idHabilidade;
    private Long idUsuario;
    private String nome;
    private String categoriaSlug;
    private String subcategoriaSlug;
    private String nivel;
    private LocalDateTime dataCriacao;
    
    // Construtores
    public Habilidade() {
        this.dataCriacao = LocalDateTime.now();
    }
    
    public Habilidade(Long idUsuario, String nome, String categoriaSlug, String subcategoriaSlug, String nivel) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.categoriaSlug = categoriaSlug;
        this.subcategoriaSlug = subcategoriaSlug;
        this.nivel = nivel;
        this.dataCriacao = LocalDateTime.now();
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
    
    public String getCategoriaSlug() {
        return categoriaSlug;
    }
    
    public void setCategoriaSlug(String categoriaSlug) {
        this.categoriaSlug = categoriaSlug;
    }
    
    public String getSubcategoriaSlug() {
        return subcategoriaSlug;
    }
    
    public void setSubcategoriaSlug(String subcategoriaSlug) {
        this.subcategoriaSlug = subcategoriaSlug;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}

