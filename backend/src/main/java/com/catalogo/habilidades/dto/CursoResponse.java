package com.catalogo.habilidades.dto;

public class CursoResponse {
    
    private Long id;
    private String idAlura;
    private String nome;
    private String categoria;
    private String link;
    private String descricao;
    private Integer duracaoHoras;
    private String nivel;
    private String instrutor;
    private String imagemUrl;
    private String origem;
    private Boolean concluido;
    private Integer progressoPercentual;
    
    public CursoResponse() {
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIdAlura() {
        return idAlura;
    }
    
    public void setIdAlura(String idAlura) {
        this.idAlura = idAlura;
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
    
    public String getInstrutor() {
        return instrutor;
    }
    
    public void setInstrutor(String instrutor) {
        this.instrutor = instrutor;
    }
    
    public String getImagemUrl() {
        return imagemUrl;
    }
    
    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
    
    public String getOrigem() {
        return origem;
    }
    
    public void setOrigem(String origem) {
        this.origem = origem;
    }
    
    public Boolean getConcluido() {
        return concluido;
    }
    
    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }
    
    public Integer getProgressoPercentual() {
        return progressoPercentual;
    }
    
    public void setProgressoPercentual(Integer progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }
}

