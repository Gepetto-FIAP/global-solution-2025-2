/**
 * Serviço para comunicação com a API da Alura (via proxy do backend)
 */

import { apiGet } from '../api-client';
import { AluraCategory, AluraCourse } from '../types';

/**
 * Busca todas as categorias da Alura
 */
export async function getAluraCategorias(): Promise<AluraCategory[]> {
  try {
    const response = await apiGet<AluraCategory[]>('/api/alura/categorias');
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao buscar categorias da Alura:', response.message);
    return [];
  } catch (error) {
    console.error('Erro ao buscar categorias da Alura:', error);
    return [];
  }
}

/**
 * Busca todos os cursos da Alura
 */
export async function getAluraCursos(
  categoriaSlug?: string,
  subcategoriaSlug?: string
): Promise<AluraCourse[]> {
  try {
    let endpoint = '/api/alura/cursos';
    
    const params = new URLSearchParams();
    if (categoriaSlug) params.append('categoria', categoriaSlug);
    if (subcategoriaSlug) params.append('subcategoria', subcategoriaSlug);
    
    if (params.toString()) {
      endpoint += `?${params.toString()}`;
    }
    
    const response = await apiGet<AluraCourse[]>(endpoint);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao buscar cursos da Alura:', response.message);
    return [];
  } catch (error) {
    console.error('Erro ao buscar cursos da Alura:', error);
    return [];
  }
}

/**
 * Busca detalhes de um curso específico por slug
 */
export async function getAluraCursoBySlug(slug: string): Promise<AluraCourse | null> {
  try {
    const response = await apiGet<AluraCourse>(`/api/alura/curso/${slug}`);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao buscar curso da Alura:', response.message);
    return null;
  } catch (error) {
    console.error('Erro ao buscar curso da Alura:', error);
    return null;
  }
}

