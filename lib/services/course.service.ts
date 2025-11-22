/**
 * Serviço para gerenciamento de inscrições em cursos
 */

import { apiGet, apiPost } from '../api-client';
import { CourseEnrollment, DashboardStats } from '../types';
import { getAuthToken } from '../auth';

/**
 * Inscreve um usuário em um curso
 */
export async function enrollInCourse(enrollment: Partial<CourseEnrollment>): Promise<CourseEnrollment | null> {
  try {
    const token = getAuthToken();
    const response = await apiPost<CourseEnrollment>('/api/cursos/inscrever', enrollment, token);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao inscrever no curso:', response.message);
    return null;
  } catch (error) {
    console.error('Erro ao inscrever no curso:', error);
    return null;
  }
}

/**
 * Busca todas as inscrições de um usuário
 */
export async function getEnrollmentsByUser(userId: number): Promise<CourseEnrollment[]> {
  try {
    const token = getAuthToken();
    const response = await apiGet<CourseEnrollment[]>(`/api/cursos/inscricoes/${userId}`, token);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao buscar inscrições:', response.message);
    return [];
  } catch (error) {
    console.error('Erro ao buscar inscrições:', error);
    return [];
  }
}

/**
 * Busca inscrições em progresso de um usuário
 */
export async function getEnrollmentsInProgress(userId: number): Promise<CourseEnrollment[]> {
  try {
    const token = getAuthToken();
    const response = await apiGet<CourseEnrollment[]>(`/api/cursos/inscricoes/${userId}/em-progresso`, token);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao buscar inscrições em progresso:', response.message);
    return [];
  } catch (error) {
    console.error('Erro ao buscar inscrições em progresso:', error);
    return [];
  }
}

/**
 * Atualiza o progresso de uma inscrição
 */
export async function updateEnrollmentProgress(
  enrollmentId: number,
  hoursStudied: number
): Promise<CourseEnrollment | null> {
  try {
    const token = getAuthToken();
    const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
    
    const response = await fetch(`${API_BASE_URL}/api/cursos/${enrollmentId}/progresso`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({ horasEstudadas: hoursStudied }),
    });
    
    if (response.ok) {
      return await response.json();
    }
    
    console.error('Erro ao atualizar progresso');
    return null;
  } catch (error) {
    console.error('Erro ao atualizar progresso:', error);
    return null;
  }
}

/**
 * Marca um curso como concluído
 */
export async function completeCourse(enrollmentId: number): Promise<CourseEnrollment | null> {
  try {
    const token = getAuthToken();
    const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
    
    const response = await fetch(`${API_BASE_URL}/api/cursos/${enrollmentId}/completar`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    });
    
    if (response.ok) {
      return await response.json();
    }
    
    console.error('Erro ao completar curso');
    return null;
  } catch (error) {
    console.error('Erro ao completar curso:', error);
    return null;
  }
}

/**
 * Busca estatísticas do dashboard do usuário
 */
export async function getDashboardStats(userId: number): Promise<DashboardStats | null> {
  try {
    const token = getAuthToken();
    const response = await apiGet<DashboardStats>(`/api/dashboard/stats/${userId}`, token);
    
    if (response.success && response.data) {
      return response.data;
    }
    
    console.error('Erro ao buscar estatísticas:', response.message);
    return null;
  } catch (error) {
    console.error('Erro ao buscar estatísticas:', error);
    return null;
  }
}

