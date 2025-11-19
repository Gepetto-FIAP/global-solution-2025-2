package com.catalogo.habilidades.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class MetaMensalRequest {
    
    @Min(value = 1, message = "Mês deve ser entre 1 e 12")
    @Max(value = 12, message = "Mês deve ser entre 1 e 12")
    private Integer mes;
    
    @Min(value = 2020, message = "Ano inválido")
    private Integer ano;
    
    @Min(value = 1, message = "Meta de cursos deve ser no mínimo 1")
    private Integer metaCursos;
    
    // Getters e Setters
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
}

