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

