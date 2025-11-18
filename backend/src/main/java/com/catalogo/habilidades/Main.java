package com.catalogo.habilidades;

import com.catalogo.habilidades.config.JaxRsApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Classe principal para iniciar o servidor Grizzly embutido
 */
public class Main {
    
    // Base URI para a aplicação
    public static final String BASE_URI = "http://localhost:8080/";
    
    /**
     * Inicia o servidor Grizzly HTTP expondo os recursos JAX-RS definidos na aplicação
     */
    public static HttpServer startServer() {
        // Criar ResourceConfig que registra os recursos JAX-RS
        final ResourceConfig rc = ResourceConfig.forApplicationClass(JaxRsApplication.class);
        
        // Criar e iniciar o servidor Grizzly HTTP
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
    
    /**
     * Método main para iniciar a aplicação
     */
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando Catálogo de Habilidades API...");
            System.out.println("Servidor disponível em: " + BASE_URI);
            System.out.println("API disponível em: " + BASE_URI + "api/");
            
            final HttpServer server = startServer();
            
            // Adicionar shutdown hook para encerrar o servidor graciosamente
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nEncerrando servidor...");
                server.shutdownNow();
                com.catalogo.habilidades.config.PersistenceConfig.close();
                System.out.println("Servidor encerrado.");
            }));
            
            System.out.println("\nPressione Ctrl+C para encerrar o servidor...\n");
            
            // Manter o servidor rodando
            Thread.currentThread().join();
            
        } catch (InterruptedException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

