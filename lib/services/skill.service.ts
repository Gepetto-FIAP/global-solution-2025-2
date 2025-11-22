/**
 * Serviço para gerenciamento de habilidades
 */

import { apiGet, apiPost } from '../api-client';
import { Skill } from '../types';
import { getAuthToken } from '../auth';

/**
 * Busca todas as habilidades de um usuário
 */
export async function getSkillsByUser(userId: number): Promise<Skill[]> {
  try {
    const token = getAuthToken();
    const response = await apiGet<Skill[]>(`/api/habilidades/usuario/${userId}`, token);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao buscar habilidades:', response.message);
    return [];
  } catch (error) {
    console.error('Erro ao buscar habilidades:', error);
    return [];
  }
}

/**
 * Cria uma nova habilidade
 */
export async function createSkill(skill: Skill): Promise<Skill | null> {
  try {
    const token = getAuthToken();
    const response = await apiPost<Skill>('/api/habilidades', skill, token);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao criar habilidade:', response.message);
    return null;
  } catch (error) {
    console.error('Erro ao criar habilidade:', error);
    return null;
  }
}

/**
 * Atualiza uma habilidade existente
 */
export async function updateSkill(skillId: number, skill: Partial<Skill>): Promise<Skill | null> {
  try {
    const token = getAuthToken();
    const response = await apiPost<Skill>(`/api/habilidades/${skillId}`, skill, token);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao atualizar habilidade:', response.message);
    return null;
  } catch (error) {
    console.error('Erro ao atualizar habilidade:', error);
    return null;
  }
}

/**
 * Deleta uma habilidade
 */
export async function deleteSkill(skillId: number): Promise<boolean> {
  try {
    const token = getAuthToken();
    const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
    
    const response = await fetch(`${API_BASE_URL}/api/habilidades/${skillId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
    
    return response.ok;
  } catch (error) {
    console.error('Erro ao deletar habilidade:', error);
    return false;
  }
}

