package com.catalogo.habilidades.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ProgressoCursoRequest {
    
    @Min(value = 0, message = "Progresso deve ser no mínimo 0")
    @Max(value = 100, message = "Progresso deve ser no máximo 100")
    private Integer progressoPercentual;
    
    private Boolean concluido;
    
    // Getters e Setters
    public Integer getProgressoPercentual() {
        return progressoPercentual;
    }
    
    public void setProgressoPercentual(Integer progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }
    
    public Boolean getConcluido() {
        return concluido;
    }
    
    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }
}

