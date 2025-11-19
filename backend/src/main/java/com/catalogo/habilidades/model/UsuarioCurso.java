package com.catalogo.habilidades.model;

import java.time.LocalDateTime;

public class UsuarioCurso {
    
    private Long idUsuarioCurso;
    private Long idUsuario;
    private Long idCurso;
    private String idCursoAlura;
    private Integer progressoPercentual; // 0-100
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private Boolean concluido;
    private Integer xpGanho;
    
    // Construtores
    public UsuarioCurso() {
        this.progressoPercentual = 0;
        this.concluido = false;
        this.xpGanho = 0;
        this.dataInicio = LocalDateTime.now();
    }
    
    public UsuarioCurso(Long idUsuario, Long idCurso) {
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
        this.progressoPercentual = 0;
        this.concluido = false;
        this.xpGanho = 0;
        this.dataInicio = LocalDateTime.now();
    }
    
    public UsuarioCurso(Long idUsuario, String idCursoAlura) {
        this.idUsuario = idUsuario;
        this.idCursoAlura = idCursoAlura;
        this.progressoPercentual = 0;
        this.concluido = false;
        this.xpGanho = 0;
        this.dataInicio = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getIdUsuarioCurso() {
        return idUsuarioCurso;
    }
    
    public void setIdUsuarioCurso(Long idUsuarioCurso) {
        this.idUsuarioCurso = idUsuarioCurso;
    }
    
    public Long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public Long getIdCurso() {
        return idCurso;
    }
    
    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }
    
    public String getIdCursoAlura() {
        return idCursoAlura;
    }
    
    public void setIdCursoAlura(String idCursoAlura) {
        this.idCursoAlura = idCursoAlura;
    }
    
    public Integer getProgressoPercentual() {
        return progressoPercentual;
    }
    
    public void setProgressoPercentual(Integer progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }
    
    public LocalDateTime getDataInicio() {
        return dataInicio;
    }
    
    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }
    
    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
    
    public Boolean getConcluido() {
        return concluido;
    }
    
    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }
    
    public Integer getXpGanho() {
        return xpGanho;
    }
    
    public void setXpGanho(Integer xpGanho) {
        this.xpGanho = xpGanho;
    }
}

