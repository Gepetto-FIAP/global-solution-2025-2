package com.catalogo.habilidades.service;

import com.catalogo.habilidades.model.Curso;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AluraApiService {
    
    private static final String ALURA_API_BASE_URL = "https://www.alura.com.br/api";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Busca todos os cursos da Alura
     */
    public List<Curso> buscarTodosCursos() {
        try {
            String url = ALURA_API_BASE_URL + "/cursos";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseCursosFromJson(response.body());
            } else {
                System.err.println("Erro ao buscar cursos da Alura. Status: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao buscar cursos da Alura: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Busca cursos por palavras-chave
     */
    public List<Curso> buscarCursosPorPalavrasChave(String palavrasChave) {
        List<Curso> todosCursos = buscarTodosCursos();
        List<Curso> cursosFiltrados = new ArrayList<>();
        
        String[] palavras = palavrasChave.toLowerCase().split("\\s+");
        
        for (Curso curso : todosCursos) {
            boolean matches = false;
            String nomeLower = curso.getNome() != null ? curso.getNome().toLowerCase() : "";
            String descricaoLower = curso.getDescricao() != null ? curso.getDescricao().toLowerCase() : "";
            String categoriaLower = curso.getCategoria() != null ? curso.getCategoria().toLowerCase() : "";
            
            for (String palavra : palavras) {
                if (nomeLower.contains(palavra) || 
                    descricaoLower.contains(palavra) || 
                    categoriaLower.contains(palavra)) {
                    matches = true;
                    break;
                }
            }
            
            if (matches) {
                cursosFiltrados.add(curso);
            }
        }
        
        return cursosFiltrados;
    }
    
    /**
     * Busca curso específico por ID ou slug
     */
    public Curso buscarCursoPorId(String id) {
        try {
            String url = ALURA_API_BASE_URL + "/cursos/" + id;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseCursoFromJson(response.body());
            } else {
                System.err.println("Erro ao buscar curso da Alura. Status: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao buscar curso da Alura: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Parse JSON de lista de cursos
     */
    private List<Curso> parseCursosFromJson(String json) {
        List<Curso> cursos = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            
            // A API da Alura pode retornar os cursos em diferentes formatos
            // Vamos tentar diferentes estruturas comuns
            JsonNode cursosNode = rootNode;
            
            if (rootNode.has("data")) {
                cursosNode = rootNode.get("data");
            } else if (rootNode.has("cursos")) {
                cursosNode = rootNode.get("cursos");
            } else if (rootNode.isArray()) {
                cursosNode = rootNode;
            }
            
            if (cursosNode.isArray()) {
                for (JsonNode cursoNode : cursosNode) {
                    Curso curso = parseCursoNode(cursoNode);
                    if (curso != null) {
                        cursos.add(curso);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao parsear JSON de cursos: " + e.getMessage());
        }
        
        return cursos;
    }
    
    /**
     * Parse JSON de um único curso
     */
    private Curso parseCursoFromJson(String json) {
        try {
            JsonNode cursoNode = objectMapper.readTree(json);
            
            // Se tem "data", pegar de dentro
            if (cursoNode.has("data")) {
                cursoNode = cursoNode.get("data");
            }
            
            return parseCursoNode(cursoNode);
        } catch (Exception e) {
            System.err.println("Erro ao parsear JSON de curso: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Parse um nó JSON para Curso
     */
    private Curso parseCursoNode(JsonNode node) {
        try {
            Curso curso = new Curso();
            curso.setOrigem("ALURA");
            
            // ID da Alura
            if (node.has("id")) {
                curso.setIdAlura(String.valueOf(node.get("id").asLong()));
            } else if (node.has("slug")) {
                curso.setIdAlura(node.get("slug").asText());
            }
            
            // Nome
            if (node.has("nome")) {
                curso.setNome(node.get("nome").asText());
            } else if (node.has("name")) {
                curso.setNome(node.get("name").asText());
            } else if (node.has("title")) {
                curso.setNome(node.get("title").asText());
            }
            
            // Slug
            if (node.has("slug")) {
                curso.setSlug(node.get("slug").asText());
                if (curso.getLink() == null) {
                    curso.setLink("https://www.alura.com.br/curso-online-" + curso.getSlug());
                }
            }
            
            // Link
            if (node.has("link")) {
                curso.setLink(node.get("link").asText());
            } else if (node.has("url")) {
                curso.setLink(node.get("url").asText());
            }
            
            // Descrição
            if (node.has("descricao")) {
                curso.setDescricao(node.get("descricao").asText());
            } else if (node.has("description")) {
                curso.setDescricao(node.get("description").asText());
            } else if (node.has("resumo")) {
                curso.setDescricao(node.get("resumo").asText());
            }
            
            // Duração
            if (node.has("duracao_horas")) {
                curso.setDuracaoHoras(node.get("duracao_horas").asInt());
            } else if (node.has("duracao")) {
                curso.setDuracaoHoras(node.get("duracao").asInt());
            } else if (node.has("horas")) {
                curso.setDuracaoHoras(node.get("horas").asInt());
            }
            
            // Nível
            if (node.has("nivel")) {
                curso.setNivel(node.get("nivel").asText());
            } else if (node.has("level")) {
                curso.setNivel(node.get("level").asText());
            }
            
            // Categoria
            if (node.has("categoria")) {
                curso.setCategoria(node.get("categoria").asText());
            } else if (node.has("category")) {
                curso.setCategoria(node.get("category").asText());
            } else if (node.has("categoria_alura")) {
                curso.setCategoriaAlura(node.get("categoria_alura").asText());
            }
            
            // Instrutor
            if (node.has("instrutor")) {
                curso.setInstrutor(node.get("instrutor").asText());
            } else if (node.has("instructor")) {
                curso.setInstrutor(node.get("instructor").asText());
            } else if (node.has("autor")) {
                curso.setInstrutor(node.get("autor").asText());
            }
            
            // Imagem
            if (node.has("imagem_url")) {
                curso.setImagemUrl(node.get("imagem_url").asText());
            } else if (node.has("imagem")) {
                curso.setImagemUrl(node.get("imagem").asText());
            } else if (node.has("image")) {
                curso.setImagemUrl(node.get("image").asText());
            }
            
            // Se não tem nome, não é um curso válido
            if (curso.getNome() == null || curso.getNome().isEmpty()) {
                return null;
            }
            
            return curso;
        } catch (Exception e) {
            System.err.println("Erro ao parsear nó de curso: " + e.getMessage());
            return null;
        }
    }
}

