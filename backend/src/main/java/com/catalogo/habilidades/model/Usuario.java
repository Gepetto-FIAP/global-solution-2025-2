package com.catalogo.habilidades.model;

import java.time.LocalDateTime;

public class Usuario {
    
    private Long idUsuario;
    private String nome;
    private String email;
    private String passwordHash;
    private LocalDateTime dataCadastro;
    
    // Construtores
    public Usuario() {
        this.dataCadastro = LocalDateTime.now();
    }
    
    public Usuario(String nome, String email, String passwordHash) {
        this.nome = nome;
        this.email = email;
        this.passwordHash = passwordHash;
        this.dataCadastro = LocalDateTime.now();
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
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}

