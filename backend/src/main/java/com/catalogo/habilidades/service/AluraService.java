package com.catalogo.habilidades.service;

import com.catalogo.habilidades.dto.AluraCategoriaDTO;
import com.catalogo.habilidades.dto.AluraCursoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AluraService {
    
    private static final String ALURA_API_BASE_URL = "https://www.alura.com.br/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public AluraService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * Busca todas as categorias da Alura
     * Atualiza o número de cursos com o total disponível na categoria
     */
    public List<AluraCategoriaDTO> getCategorias() {
        try {
            String url = ALURA_API_BASE_URL + "/categorias";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                AluraCategoriaDTO[] categorias = objectMapper.readValue(response.body(), AluraCategoriaDTO[].class);
                
                // Atualizar numeroCursos com o total de cursos disponíveis
                for (AluraCategoriaDTO categoria : categorias) {
                    if (categoria.getCursos() != null && !categoria.getCursos().isEmpty()) {
                        categoria.setNumeroCursos(categoria.getCursos().size());
                    } else {
                        categoria.setNumeroCursos(0);
                    }
                }
                
                return List.of(categorias);
            } else {
                System.err.println("Erro ao buscar categorias da Alura. Status: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao fazer requisição para API da Alura: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Busca todos os cursos da Alura
     * Busca as categorias e para cada curso nas subcategorias, faz uma chamada individual para obter detalhes
     */
    public List<AluraCursoDTO> getCursos() {
        List<AluraCategoriaDTO> categorias = getCategorias();
        List<AluraCursoDTO> todosOsCursos = new ArrayList<>();
        
        for (AluraCategoriaDTO categoria : categorias) {
            if (categoria.getSubcategorias() != null) {
                for (AluraCategoriaDTO.AluraSubcategoriaDTO subcategoria : categoria.getSubcategorias()) {
                    if (subcategoria.getCursos() != null) {
                        for (AluraCategoriaDTO.AluraCursoResumoDTO cursoResumo : subcategoria.getCursos()) {
                            // Buscar detalhes completos do curso
                            AluraCursoDTO cursoCompleto = getCursoBySlug(cursoResumo.getSlug());
                            if (cursoCompleto != null) {
                                // Garantir que categoria e subcategoria estejam preenchidas
                                if (cursoCompleto.getCategoria() == null) {
                                    cursoCompleto.setCategoria(categoria.getSlug());
                                }
                                if (cursoCompleto.getSubcategoria() == null) {
                                    cursoCompleto.setSubcategoria(subcategoria.getSlug());
                                }
                                // Usar carga_horaria como tempoEstimado se não estiver preenchido
                                if (cursoCompleto.getTempoEstimado() == null && cursoCompleto.getCargaHoraria() != null) {
                                    cursoCompleto.setTempoEstimado(cursoCompleto.getCargaHoraria());
                                }
                                // Usar metadescription como descricao se descricao não estiver preenchida
                                if (cursoCompleto.getDescricao() == null && cursoCompleto.getMetadescription() != null) {
                                    cursoCompleto.setDescricao(cursoCompleto.getMetadescription());
                                }
                                todosOsCursos.add(cursoCompleto);
                            }
                        }
                    }
                }
            }
        }
        
        return todosOsCursos;
    }
    
    /**
     * Busca detalhes de um curso específico por slug
     */
    public AluraCursoDTO getCursoBySlug(String slug) {
        try {
            // A API da Alura usa o formato: curso-{slug}
            String cursoSlug = slug.startsWith("curso-") ? slug : "curso-" + slug;
            String url = ALURA_API_BASE_URL + "/" + cursoSlug;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                AluraCursoDTO curso = objectMapper.readValue(response.body(), AluraCursoDTO.class);
                
                // Extrair categoria e subcategoria da resposta se existirem
                try {
                    com.fasterxml.jackson.databind.JsonNode rootNode = objectMapper.readTree(response.body());
                    
                    // Extrair categoria
                    if (rootNode.has("categoria") && rootNode.get("categoria").has("slug")) {
                        curso.setCategoria(rootNode.get("categoria").get("slug").asText());
                    }
                    
                    // Extrair subcategoria
                    if (rootNode.has("subcategoria") && rootNode.get("subcategoria").has("slug")) {
                        curso.setSubcategoria(rootNode.get("subcategoria").get("slug").asText());
                    }
                    
                    // Extrair carga_horaria
                    if (rootNode.has("carga_horaria")) {
                        curso.setCargaHoraria(rootNode.get("carga_horaria").asInt());
                    }
                    
                    // Extrair quantidade_aulas
                    if (rootNode.has("quantidade_aulas")) {
                        curso.setQuantidadeAulas(rootNode.get("quantidade_aulas").asInt());
                    }
                    
                    // Extrair quantidade_alunos
                    if (rootNode.has("quantidade_alunos")) {
                        curso.setQuantidadeAlunos(rootNode.get("quantidade_alunos").asInt());
                    }
                    
                } catch (Exception e) {
                    System.err.println("Erro ao extrair campos adicionais do curso: " + e.getMessage());
                }
                
                return curso;
            } else {
                System.err.println("Erro ao buscar curso da Alura. Status: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao fazer requisição para API da Alura: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Filtra cursos por categoria/subcategoria
     * Se categoriaSlug for "todas" ou null, retorna cursos de todas as categorias
     */
    public List<AluraCursoDTO> getCursosByCategoria(String categoriaSlug, String subcategoriaSlug) {
        List<AluraCategoriaDTO> categorias = getCategorias();
        List<AluraCursoDTO> cursosFiltrados = new ArrayList<>();
        
        boolean buscarTodas = categoriaSlug == null || categoriaSlug.equalsIgnoreCase("todas");
        
        for (AluraCategoriaDTO categoria : categorias) {
            // Se uma categoria específica foi solicitada, pular outras categorias
            if (!buscarTodas && !categoria.getSlug().equalsIgnoreCase(categoriaSlug)) {
                continue;
            }
            
            // Os cursos estão diretamente no campo "cursos" da categoria
            if (categoria.getCursos() != null && !categoria.getCursos().isEmpty()) {
                System.out.println("Processando " + categoria.getCursos().size() + " cursos da categoria " + categoria.getSlug());
                
                // Processar todos os cursos (sem limite)
                for (AluraCategoriaDTO.AluraCursoResumoDTO cursoResumo : categoria.getCursos()) {
                    // Criar DTO com os dados reais da API
                    AluraCursoDTO curso = new AluraCursoDTO();
                    curso.setNome(cursoResumo.getNome());
                    curso.setSlug(cursoResumo.getSlug());
                    curso.setCategoria(categoria.getSlug());
                    curso.setUrl("https://www.alura.com.br/curso-" + cursoResumo.getSlug());
                    
                    // Usar tempo_estimado real da API (ou 8h como fallback)
                    Integer tempoEstimado = cursoResumo.getTempoEstimado();
                    curso.setTempoEstimado(tempoEstimado != null ? tempoEstimado : 8);
                    
                    curso.setDescricao("Curso de " + cursoResumo.getNome());
                    
                    // Não definir nível (será null)
                    curso.setNivel(null);
                    
                    // Filtrar por subcategoria se fornecida
                    if (subcategoriaSlug == null || subcategoriaSlug.isEmpty()) {
                        cursosFiltrados.add(curso);
                    }
                }
            }
        }
        
        System.out.println("Total de cursos retornados: " + cursosFiltrados.size() + " (categoria: " + (buscarTodas ? "TODAS" : categoriaSlug) + ")");
        return cursosFiltrados;
    }
}

