/**
 * Serviços de progresso
 * Consome APIs REST do backend Java
 */

import { apiPut, apiPost, ApiResponse } from '../api-client';
import { getAuthToken } from '../auth';

export interface ProgressoCursoRequest {
  progressoPercentual?: number;
  concluido?: boolean;
}

export interface UsuarioCurso {
  idUsuarioCurso: number;
  idUsuario: number;
  idCurso?: number;
  idCursoAlura?: string;
  progressoPercentual: number;
  concluido: boolean;
  xpGanho: number;
}

/**
 * Atualiza progresso de um curso
 */
export async function atualizarProgressoCurso(
  idCurso: number,
  request: ProgressoCursoRequest
): Promise<UsuarioCurso> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiPut<UsuarioCurso>(`/progresso/curso/${idCurso}`, request, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao atualizar progresso');
}

/**
 * Atualiza progresso de um curso da Alura
 */
export async function atualizarProgressoCursoAlura(
  idCursoAlura: string,
  request: ProgressoCursoRequest
): Promise<UsuarioCurso> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiPut<UsuarioCurso>(`/progresso/curso-alura/${idCursoAlura}`, request, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao atualizar progresso');
}

/**
 * Marca curso como concluído
 */
export async function marcarCursoComoConcluido(idCurso: number): Promise<UsuarioCurso> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiPost<UsuarioCurso>(`/progresso/curso/${idCurso}/concluir`, {}, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao marcar curso como concluído');
}

/**
 * Marca curso da Alura como concluído
 */
export async function marcarCursoAluraComoConcluido(idCursoAlura: string): Promise<UsuarioCurso> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiPost<UsuarioCurso>(`/progresso/curso-alura/${idCursoAlura}/concluir`, {}, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao marcar curso como concluído');
}

