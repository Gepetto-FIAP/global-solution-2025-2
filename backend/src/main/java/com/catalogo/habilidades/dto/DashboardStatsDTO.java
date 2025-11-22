package com.catalogo.habilidades.dto;

public class DashboardStatsDTO {
    
    private Long idUsuario;
    private String nome;
    private String email;
    private Integer xpTotal;
    private Integer totalHabilidades;
    private Integer totalInscricoes;
    private Integer cursosCompletos;
    private Integer horasTotais;
    
    // Construtores
    public DashboardStatsDTO() {}
    
    public DashboardStatsDTO(Long idUsuario, String nome, String email, Integer xpTotal) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.xpTotal = xpTotal;
    }
    
    // Getters e Setters
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getXpTotal() {
        return xpTotal;
    }
    
    public void setXpTotal(Integer xpTotal) {
        this.xpTotal = xpTotal;
    }
    
    public Integer getTotalHabilidades() {
        return totalHabilidades;
    }
    
    public void setTotalHabilidades(Integer totalHabilidades) {
        this.totalHabilidades = totalHabilidades;
    }
    
    public Integer getTotalInscricoes() {
        return totalInscricoes;
    }
    
    public void setTotalInscricoes(Integer totalInscricoes) {
        this.totalInscricoes = totalInscricoes;
    }
    
    public Integer getCursosCompletos() {
        return cursosCompletos;
    }
    
    public void setCursosCompletos(Integer cursosCompletos) {
        this.cursosCompletos = cursosCompletos;
    }
    
    public Integer getHorasTotais() {
        return horasTotais;
    }
    
    public void setHorasTotais(Integer horasTotais) {
        this.horasTotais = horasTotais;
    }
}

