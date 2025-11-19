package com.catalogo.habilidades.model;

public class MetaMensal {
    
    private Long idMeta;
    private Long idUsuario;
    private Integer mes; // 1-12
    private Integer ano;
    private Integer metaCursos;
    private Integer cursosConcluidos;
    
    // Construtores
    public MetaMensal() {
        this.metaCursos = 10;
        this.cursosConcluidos = 0;
    }
    
    public MetaMensal(Long idUsuario, Integer mes, Integer ano, Integer metaCursos) {
        this.idUsuario = idUsuario;
        this.mes = mes;
        this.ano = ano;
        this.metaCursos = metaCursos;
        this.cursosConcluidos = 0;
    }
    
    // Getters e Setters
    public Long getIdMeta() {
        return idMeta;
    }
    
    public void setIdMeta(Long idMeta) {
        this.idMeta = idMeta;
    }
    
    public Long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public Integer getMes() {
        return mes;
    }
    
    public void setMes(Integer mes) {
        this.mes = mes;
    }
    
    public Integer getAno() {
        return ano;
    }
    
    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    public Integer getMetaCursos() {
        return metaCursos;
    }
    
    public void setMetaCursos(Integer metaCursos) {
        this.metaCursos = metaCursos;
    }
    
    public Integer getCursosConcluidos() {
        return cursosConcluidos;
    }
    
    public void setCursosConcluidos(Integer cursosConcluidos) {
        this.cursosConcluidos = cursosConcluidos;
    }
}

