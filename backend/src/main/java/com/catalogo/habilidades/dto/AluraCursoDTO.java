package com.catalogo.habilidades.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AluraCursoDTO {
    
    private String nome;
    private String slug;
    private String descricao;
    private String metadescription;
    private Integer tempoEstimado;
    
    // Alias para compatibilidade com o frontend
    @JsonProperty("duracaoHoras")
    private Integer duracaoHoras;
    
    private Integer cargaHoraria;
    private String categoria;
    private String subcategoria;
    private String nivel;
    private String url;
    private Integer quantidadeAulas;
    private Integer quantidadeAlunos;
    private Double nota;
    
    // Construtores
    public AluraCursoDTO() {}
    
    public AluraCursoDTO(String nome, String slug, Integer tempoEstimado) {
        this.nome = nome;
        this.slug = slug;
        this.tempoEstimado = tempoEstimado;
        this.duracaoHoras = tempoEstimado; // Sincronizar
    }
    
    // Getters e Setters
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getMetadescription() {
        return metadescription;
    }
    
    public void setMetadescription(String metadescription) {
        this.metadescription = metadescription;
    }
    
    public Integer getTempoEstimado() {
        return tempoEstimado;
    }
    
    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
        this.duracaoHoras = tempoEstimado; // Sincronizar
    }
    
    public Integer getDuracaoHoras() {
        return duracaoHoras != null ? duracaoHoras : tempoEstimado;
    }
    
    public void setDuracaoHoras(Integer duracaoHoras) {
        this.duracaoHoras = duracaoHoras;
        if (this.tempoEstimado == null) {
            this.tempoEstimado = duracaoHoras;
        }
    }
    
    public Integer getCargaHoraria() {
        return cargaHoraria;
    }
    
    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getSubcategoria() {
        return subcategoria;
    }
    
    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Integer getQuantidadeAulas() {
        return quantidadeAulas;
    }
    
    public void setQuantidadeAulas(Integer quantidadeAulas) {
        this.quantidadeAulas = quantidadeAulas;
    }
    
    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }
    
    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }
    
    public Double getNota() {
        return nota;
    }
    
    public void setNota(Double nota) {
        this.nota = nota;
    }
}

