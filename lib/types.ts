/**
 * Tipos TypeScript para autenticação
 * Comunicação com backend Java REST API
 */

export interface User {
  id: number;
  nome: string;
  email: string;
  xpTotal?: number;
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

// Alura API Types
export interface AluraCategory {
  nome: string;
  slug: string;
  ordem?: number;
  cor?: string;
  corAuxiliar?: string;
  subcategorias: AluraSubcategory[];
  numeroCursos?: number;
}

export interface AluraSubcategory {
  slug: string;
  nome: string;
  metaTitle?: string;
  metaDescription?: string;
  description?: string;
  guides?: string;
}

export interface AluraCourse {
  nome: string;
  slug: string;
  descricao?: string;
  metadescription?: string;
  tempoEstimado?: number;
  duracaoHoras?: number;
  cargaHoraria?: number;
  categoria?: string;
  subcategoria?: string;
  nivel?: string;
  url?: string;
  link?: string;
  quantidadeAulas?: number;
  quantidadeAlunos?: number;
  nota?: number;
}

// Skills & Courses Types
export interface Skill {
  idHabilidade?: number;
  idUsuario: number;
  nome: string;
  categoriaSlug: string;
  subcategoriaSlug?: string;
  nivel: 'Iniciante' | 'Intermediário' | 'Avançado' | 'Expert';
  dataCriacao?: string;
  totalCursos?: number;
  cursosCompletos?: number;
}

export interface CourseEnrollment {
  idInscricao?: number;
  idUsuario: number;
  idHabilidade?: number;
  cursoSlug: string;
  cursoNome: string;
  tempoEstimado: number;
  horasEstudadas: number;
  completado: boolean;
  progressoPercentual?: number;
  dataInscricao?: string;
  dataConclusao?: string;
  habilidadeNome?: string;
}

export interface DashboardStats {
  idUsuario: number;
  nome: string;
  email: string;
  xpTotal: number;
  totalHabilidades: number;
  totalInscricoes: number;
  cursosCompletos: number;
  horasTotais: number;
}

