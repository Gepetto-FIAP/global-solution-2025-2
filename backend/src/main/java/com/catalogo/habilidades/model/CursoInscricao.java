package com.catalogo.habilidades.model;

import java.time.LocalDateTime;

public class CursoInscricao {
    
    private Long idInscricao;
    private Long idUsuario;
    private Long idHabilidade;
    private String cursoSlug;
    private String cursoNome;
    private Integer tempoEstimado;
    private Integer horasEstudadas;
    private Boolean completado;
    private LocalDateTime dataInscricao;
    private LocalDateTime dataConclusao;
    
    // Construtores
    public CursoInscricao() {
        this.dataInscricao = LocalDateTime.now();
        this.horasEstudadas = 0;
        this.completado = false;
    }
    
    public CursoInscricao(Long idUsuario, Long idHabilidade, String cursoSlug, String cursoNome, Integer tempoEstimado) {
        this.idUsuario = idUsuario;
        this.idHabilidade = idHabilidade;
        this.cursoSlug = cursoSlug;
        this.cursoNome = cursoNome;
        this.tempoEstimado = tempoEstimado;
        this.dataInscricao = LocalDateTime.now();
        this.horasEstudadas = 0;
        this.completado = false;
    }
    
    // Getters e Setters
    public Long getIdInscricao() {
        return idInscricao;
    }
    
    public void setIdInscricao(Long idInscricao) {
        this.idInscricao = idInscricao;
    }
    
    public Long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public Long getIdHabilidade() {
        return idHabilidade;
    }
    
    public void setIdHabilidade(Long idHabilidade) {
        this.idHabilidade = idHabilidade;
    }
    
    public String getCursoSlug() {
        return cursoSlug;
    }
    
    public void setCursoSlug(String cursoSlug) {
        this.cursoSlug = cursoSlug;
    }
    
    public String getCursoNome() {
        return cursoNome;
    }
    
    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
    }
    
    public Integer getTempoEstimado() {
        return tempoEstimado;
    }
    
    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }
    
    public Integer getHorasEstudadas() {
        return horasEstudadas;
    }
    
    public void setHorasEstudadas(Integer horasEstudadas) {
        this.horasEstudadas = horasEstudadas;
    }
    
    public Boolean getCompletado() {
        return completado;
    }
    
    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }
    
    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }
    
    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }
    
    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }
    
    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
    
    // Método auxiliar para calcular progresso
    public Double getProgressoPercentual() {
        if (tempoEstimado == null || tempoEstimado == 0) {
            return 0.0;
        }
        return (horasEstudadas.doubleValue() / tempoEstimado.doubleValue()) * 100.0;
    }
}

