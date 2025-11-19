package com.catalogo.habilidades.model;

public class Curso {
    
    private Long idCurso;
    private String nome;
    private String categoria;
    private String link;
    private String descricao;
    private Integer duracaoHoras;
    private String nivel; // 'Iniciante', 'Intermediário', 'Avançado'
    private String idAlura;
    private String slug;
    private String instrutor;
    private String imagemUrl;
    private String categoriaAlura;
    private String origem; // 'ALURA' ou 'MANUAL'
    
    // Construtores
    public Curso() {
        this.origem = "MANUAL";
    }
    
    public Curso(String nome, String categoria, String link, String descricao, 
                 Integer duracaoHoras, String nivel) {
        this.nome = nome;
        this.categoria = categoria;
        this.link = link;
        this.descricao = descricao;
        this.duracaoHoras = duracaoHoras;
        this.nivel = nivel;
        this.origem = "MANUAL";
    }
    
    // Getters e Setters
    public Long getIdCurso() {
        return idCurso;
    }
    
    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
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
    
    public String getIdAlura() {
        return idAlura;
    }
    
    public void setIdAlura(String idAlura) {
        this.idAlura = idAlura;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
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
    
    public String getCategoriaAlura() {
        return categoriaAlura;
    }
    
    public void setCategoriaAlura(String categoriaAlura) {
        this.categoriaAlura = categoriaAlura;
    }
    
    public String getOrigem() {
        return origem;
    }
    
    public void setOrigem(String origem) {
        this.origem = origem;
    }
}

