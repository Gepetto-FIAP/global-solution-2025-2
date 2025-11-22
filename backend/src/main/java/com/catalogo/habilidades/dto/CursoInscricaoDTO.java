package com.catalogo.habilidades.dto;

import java.time.LocalDateTime;

public class CursoInscricaoDTO {
    
    private Long idInscricao;
    private Long idUsuario;
    private Long idHabilidade;
    private String cursoSlug;
    private String cursoNome;
    private Integer tempoEstimado;
    private Integer horasEstudadas;
    private Boolean completado;
    private Double progressoPercentual;
    private LocalDateTime dataInscricao;
    private LocalDateTime dataConclusao;
    private String habilidadeNome;
    
    // Construtores
    public CursoInscricaoDTO() {}
    
    public CursoInscricaoDTO(Long idUsuario, Long idHabilidade, String cursoSlug, String cursoNome, Integer tempoEstimado) {
        this.idUsuario = idUsuario;
        this.idHabilidade = idHabilidade;
        this.cursoSlug = cursoSlug;
        this.cursoNome = cursoNome;
        this.tempoEstimado = tempoEstimado;
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
    
    public Double getProgressoPercentual() {
        return progressoPercentual;
    }
    
    public void setProgressoPercentual(Double progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
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
    
    public String getHabilidadeNome() {
        return habilidadeNome;
    }
    
    public void setHabilidadeNome(String habilidadeNome) {
        this.habilidadeNome = habilidadeNome;
    }
}

