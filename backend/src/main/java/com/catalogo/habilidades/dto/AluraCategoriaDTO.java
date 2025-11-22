package com.catalogo.habilidades.dto;

import java.util.List;

public class AluraCategoriaDTO {
    
    private String nome;
    private String slug;
    private Integer ordem;
    private String cor;
    private String corAuxiliar;
    private List<AluraSubcategoriaDTO> subcategorias;
    private List<AluraCursoResumoDTO> cursos; // Lista de cursos na categoria
    private Integer numeroCursos; // Número de cursos disponíveis
    
    // Construtores
    public AluraCategoriaDTO() {}
    
    public AluraCategoriaDTO(String nome, String slug) {
        this.nome = nome;
        this.slug = slug;
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
    
    public Integer getOrdem() {
        return ordem;
    }
    
    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
    public String getCor() {
        return cor;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    public String getCorAuxiliar() {
        return corAuxiliar;
    }
    
    public void setCorAuxiliar(String corAuxiliar) {
        this.corAuxiliar = corAuxiliar;
    }
    
    public List<AluraSubcategoriaDTO> getSubcategorias() {
        return subcategorias;
    }
    
    public void setSubcategorias(List<AluraSubcategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }
    
    public List<AluraCursoResumoDTO> getCursos() {
        return cursos;
    }
    
    public void setCursos(List<AluraCursoResumoDTO> cursos) {
        this.cursos = cursos;
    }
    
    public Integer getNumeroCursos() {
        return numeroCursos;
    }
    
    public void setNumeroCursos(Integer numeroCursos) {
        this.numeroCursos = numeroCursos;
    }
    
    // Classe interna para Subcategorias
    public static class AluraSubcategoriaDTO {
        private String slug;
        private String nome;
        private String metaTitle;
        private String metaDescription;
        private String description;
        private String guides;
        private List<AluraCursoResumoDTO> cursos;
        
        public AluraSubcategoriaDTO() {}
        
        // Getters e Setters
        public String getSlug() {
            return slug;
        }
        
        public void setSlug(String slug) {
            this.slug = slug;
        }
        
        public String getNome() {
            return nome;
        }
        
        public void setNome(String nome) {
            this.nome = nome;
        }
        
        public String getMetaTitle() {
            return metaTitle;
        }
        
        public void setMetaTitle(String metaTitle) {
            this.metaTitle = metaTitle;
        }
        
        public String getMetaDescription() {
            return metaDescription;
        }
        
        public void setMetaDescription(String metaDescription) {
            this.metaDescription = metaDescription;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getGuides() {
            return guides;
        }
        
        public void setGuides(String guides) {
            this.guides = guides;
        }
        
        public List<AluraCursoResumoDTO> getCursos() {
            return cursos;
        }
        
        public void setCursos(List<AluraCursoResumoDTO> cursos) {
            this.cursos = cursos;
        }
    }
    
    // Classe interna para resumo de cursos dentro das subcategorias
    public static class AluraCursoResumoDTO {
        private String slug;
        private String nome;
        
        @com.fasterxml.jackson.annotation.JsonProperty("tempo_estimado")
        private Integer tempoEstimado;
        
        public AluraCursoResumoDTO() {}
        
        public String getSlug() {
            return slug;
        }
        
        public void setSlug(String slug) {
            this.slug = slug;
        }
        
        public String getNome() {
            return nome;
        }
        
        public void setNome(String nome) {
            this.nome = nome;
        }
        
        public Integer getTempoEstimado() {
            return tempoEstimado;
        }
        
        public void setTempoEstimado(Integer tempoEstimado) {
            this.tempoEstimado = tempoEstimado;
        }
    }
}

