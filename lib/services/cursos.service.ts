/**
 * Serviços de cursos
 * Consome APIs REST do backend Java
 */

import { apiGet, apiPost, ApiResponse } from '../api-client';
import { Curso } from '../types';
import { getAuthToken } from '../auth';

export interface CursoManualRequest {
  nome: string;
  categoria: string;
  link?: string;
  descricao?: string;
  duracaoHoras?: number;
  nivel?: 'Iniciante' | 'Intermediário' | 'Avançado';
}

/**
 * Lista cursos sugeridos para uma habilidade
 */
export async function listarCursosSugeridos(idHabilidade: number): Promise<Curso[]> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiGet<Curso[]>(`/habilidades/${idHabilidade}/cursos`, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao listar cursos sugeridos');
}

/**
 * Busca um curso por ID
 */
export async function buscarCursoPorId(id: number): Promise<Curso> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiGet<Curso>(`/cursos/${id}`, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao buscar curso');
}

/**
 * Cria um curso manual
 */
export async function criarCursoManual(request: CursoManualRequest): Promise<Curso> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiPost<Curso>('/cursos/manual', request, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao criar curso manual');
}

