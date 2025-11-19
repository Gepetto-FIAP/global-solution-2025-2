package com.catalogo.habilidades.dto;

public class DashboardStatsResponse {
    
    private Integer habilidadesAdquiridas;
    private Integer cursosConcluidos;
    private Integer xpTotal;
    private Integer metaCursosMensal;
    private Integer cursosConcluidosMes;
    private Integer percentualMetaMensal;
    
    public DashboardStatsResponse() {
    }
    
    public DashboardStatsResponse(Integer habilidadesAdquiridas, Integer cursosConcluidos, 
                                  Integer xpTotal, Integer metaCursosMensal, 
                                  Integer cursosConcluidosMes) {
        this.habilidadesAdquiridas = habilidadesAdquiridas;
        this.cursosConcluidos = cursosConcluidos;
        this.xpTotal = xpTotal;
        this.metaCursosMensal = metaCursosMensal;
        this.cursosConcluidosMes = cursosConcluidosMes;
        this.percentualMetaMensal = metaCursosMensal > 0 
            ? (cursosConcluidosMes * 100) / metaCursosMensal 
            : 0;
    }
    
    // Getters e Setters
    public Integer getHabilidadesAdquiridas() {
        return habilidadesAdquiridas;
    }
    
    public void setHabilidadesAdquiridas(Integer habilidadesAdquiridas) {
        this.habilidadesAdquiridas = habilidadesAdquiridas;
    }
    
    public Integer getCursosConcluidos() {
        return cursosConcluidos;
    }
    
    public void setCursosConcluidos(Integer cursosConcluidos) {
        this.cursosConcluidos = cursosConcluidos;
    }
    
    public Integer getXpTotal() {
        return xpTotal;
    }
    
    public void setXpTotal(Integer xpTotal) {
        this.xpTotal = xpTotal;
    }
    
    public Integer getMetaCursosMensal() {
        return metaCursosMensal;
    }
    
    public void setMetaCursosMensal(Integer metaCursosMensal) {
        this.metaCursosMensal = metaCursosMensal;
    }
    
    public Integer getCursosConcluidosMes() {
        return cursosConcluidosMes;
    }
    
    public void setCursosConcluidosMes(Integer cursosConcluidosMes) {
        this.cursosConcluidosMes = cursosConcluidosMes;
    }
    
    public Integer getPercentualMetaMensal() {
        return percentualMetaMensal;
    }
    
    public void setPercentualMetaMensal(Integer percentualMetaMensal) {
        this.percentualMetaMensal = percentualMetaMensal;
    }
}

