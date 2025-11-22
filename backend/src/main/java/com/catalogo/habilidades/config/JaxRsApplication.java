package com.catalogo.habilidades.config;

import com.catalogo.habilidades.controller.*;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class JaxRsApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Registrar recursos REST
        classes.add(AuthController.class);
        classes.add(AluraController.class);
        classes.add(HabilidadeController.class);
        classes.add(CursoController.class);
        classes.add(DashboardController.class);
        
        // Registrar providers (filtros, exception mappers, etc.)
        classes.add(CorsFilter.class);
        classes.add(GlobalExceptionHandler.class);
        
        // Registrar Jackson para serialização JSON
        classes.add(JacksonFeature.class);
        
        return classes;
    }
}

