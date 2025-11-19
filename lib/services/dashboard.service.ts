/**
 * Serviços de dashboard
 * Consome APIs REST do backend Java
 */

import { apiGet, ApiResponse } from '../api-client';
import { DashboardStats } from '../types';
import { getAuthToken } from '../auth';

/**
 * Obtém estatísticas do dashboard
 */
export async function obterEstatisticas(): Promise<DashboardStats> {
  const token = getAuthToken();
  if (!token) {
    throw new Error('Usuário não autenticado');
  }

  const response = await apiGet<DashboardStats>('/dashboard/stats', token);
  
  if (response.success && response.data) {
    return response.data;
  }
  
  throw new Error(response.message || 'Erro ao obter estatísticas');
}

