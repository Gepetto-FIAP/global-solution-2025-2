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
    
    /**
     * Retorna a URI base da aplicação. Usa a variável PORT se disponível (Azure),
     * caso contrário usa localhost:8080 (desenvolvimento local)
     */
    private static String getBaseUri() {
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            return "http://localhost:8080/";
        }
        return "http://0.0.0.0:" + port + "/";
    }
    
    /**
     * Inicia o servidor Grizzly HTTP expondo os recursos JAX-RS definidos na aplicação
     */
    public static HttpServer startServer() {
        String baseUri = getBaseUri();
        
        // Criar ResourceConfig que registra os recursos JAX-RS
        final ResourceConfig rc = ResourceConfig.forApplicationClass(JaxRsApplication.class);
        
        // Criar e iniciar o servidor Grizzly HTTP
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);
    }
    
    /**
     * Método main para iniciar a aplicação
     */
    public static void main(String[] args) {
        try {
            String baseUri = getBaseUri();
            
            System.out.println("Iniciando Catálogo de Habilidades API...");
            System.out.println("Servidor disponível em: " + baseUri);
            System.out.println("API disponível em: " + baseUri + "api/");
            
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

