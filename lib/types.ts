/**
 * Tipos TypeScript para autenticação
 * Comunicação com backend Java REST API
 */

export interface User {
  id: number;
  nome: string;
  email: string;
}

export interface LoginData {
  email: string;
  password: string;
}

export interface RegisterData {
  nome: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  user?: User;
  token?: string;
}

export interface ApiError {
  success: false;
  message: string;
}

export interface Habilidade {
  id: number;
  nome: string;
  categoria: string;
  descricao?: string;
  nivel?: 'Iniciante' | 'Intermediário' | 'Avançado';
  progressoPercentual: number;
}

export interface Curso {
  id?: number;
  idAlura?: string;
  nome: string;
  categoria: string;
  link?: string;
  descricao?: string;
  duracaoHoras?: number;
  nivel?: 'Iniciante' | 'Intermediário' | 'Avançado';
  instrutor?: string;
  imagemUrl?: string;
  origem?: 'ALURA' | 'MANUAL';
  concluido?: boolean;
  progressoPercentual?: number;
}

export interface ProgressoCurso {
  progressoPercentual: number;
  concluido: boolean;
}

export interface MetaMensal {
  mes: number;
  ano: number;
  metaCursos: number;
  cursosConcluidos: number;
}

export interface DashboardStats {
  habilidadesAdquiridas: number;
  cursosConcluidos: number;
  xpTotal: number;
  metaCursosMensal: number;
  cursosConcluidosMes: number;
  percentualMetaMensal: number;
}

