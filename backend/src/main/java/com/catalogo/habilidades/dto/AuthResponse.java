package com.catalogo.habilidades.dto;

public class AuthResponse {
    
    private boolean success;
    private String message;
    private UserResponse user;
    private String token;
    
    // Construtores
    public AuthResponse() {
    }
    
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public AuthResponse(boolean success, String message, UserResponse user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }
    
    // Métodos estáticos para facilitar criação
    public static AuthResponse success(String message, UserResponse user, String token) {
        return new AuthResponse(true, message, user, token);
    }
    
    public static AuthResponse error(String message) {
        return new AuthResponse(false, message);
    }
    
    // Getters e Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public UserResponse getUser() {
        return user;
    }
    
    public void setUser(UserResponse user) {
        this.user = user;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}

