package com.catalogo.habilidades.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * Configuração do Jackson para serialização/desserialização de JSON
 * Adiciona suporte a LocalDateTime e outras classes do Java 8 Time API
 */
@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {
    
    private final ObjectMapper objectMapper;
    
    public JacksonConfig() {
        objectMapper = new ObjectMapper();
        
        // Registrar módulo para suporte a LocalDateTime, LocalDate, etc.
        objectMapper.registerModule(new JavaTimeModule());
        
        // Desabilitar conversão de datas para timestamps (usar ISO-8601)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Outras configurações úteis
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
    
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}

