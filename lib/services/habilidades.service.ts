/**
 * Serviços de habilidades
 * Consome APIs REST do backend Java
 */

import { apiGet, apiPost, apiPut, apiDelete, ApiResponse } from '../api-client';
import { Habilidade } from '../types';
import { getAuthToken } from '../auth';

export interface HabilidadeRequest {
  nome: string;
  categoria: string;
  descricao?: string;
  nivel?: 'Iniciante' | 'Intermediário' | 'Avançado';
}

/**
 * Lista todas as habilidades do usuário
 */
export async function listarHabilidades(): Promise<Habilidade[]> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiGet<Habilidade[]>('/habilidades', token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao listar habilidades');
}

/**
 * Busca uma habilidade por ID
 */
export async function buscarHabilidadePorId(id: number): Promise<Habilidade> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiGet<Habilidade>(`/habilidades/${id}`, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao buscar habilidade');
}

/**
 * Cria uma nova habilidade
 */
export async function criarHabilidade(request: HabilidadeRequest): Promise<Habilidade> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiPost<Habilidade>('/habilidades', request, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao criar habilidade');
}

/**
 * Atualiza uma habilidade
 */
export async function atualizarHabilidade(id: number, request: HabilidadeRequest): Promise<Habilidade> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiPut<Habilidade>(`/habilidades/${id}`, request, token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao atualizar habilidade');
}

/**
 * Deleta uma habilidade
 */
export async function deletarHabilidade(id: number): Promise<void> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiDelete(`/habilidades/${id}`, token);
  
  if (!response.success) {
    throw new Error(response.message || 'Erro ao deletar habilidade');
  }
}

