/**
 * Serviços de autenticação
 * Consome APIs REST do backend Java
 */

import { apiPost, apiGet } from '../api-client';
import { LoginData, RegisterData, AuthResponse, User } from '../types';
import { setAuthToken, removeAuthToken, getAuthToken, setAuthUser } from '../auth';

/**
 * Realiza login do usuário
 */
export async function login(email: string, password: string): Promise<AuthResponse> {
  const loginData: LoginData = { email, password };
  const response = await apiPost<AuthResponse>('/auth/login', loginData);

  if (response.success && response.data) {
    const authResponse = response.data as AuthResponse;
    
    if (authResponse.token && authResponse.user) {
      // Salvar token e dados do usuário
      setAuthToken(authResponse.token);
      setAuthUser(authResponse.user);
      
      return {
        success: true,
        message: authResponse.message || 'Login realizado com sucesso!',
        user: authResponse.user,
        token: authResponse.token,
      };
    }
  }

  return {
    success: false,
    message: response.message || 'Erro ao realizar login',
  };
}

/**
 * Registra um novo usuário
 */
export async function register(
  nome: string,
  email: string,
  password: string
): Promise<AuthResponse> {
  const registerData: RegisterData = { nome, email, password };
  const response = await apiPost<AuthResponse>('/auth/register', registerData);

  if (response.success && response.data) {
    const authResponse = response.data as AuthResponse;
    
    if (authResponse.token && authResponse.user) {
      // Salvar token e dados do usuário
      setAuthToken(authResponse.token);
      setAuthUser(authResponse.user);
      
      return {
        success: true,
        message: authResponse.message || 'Cadastro realizado com sucesso!',
        user: authResponse.user,
        token: authResponse.token,
      };
    }
  }

  return {
    success: false,
    message: response.message || 'Erro ao realizar cadastro',
  };
}

/**
 * Realiza logout do usuário
 */
export async function logout(): Promise<{ success: boolean; message: string }> {
  const token = getAuthToken();
  
  if (token) {
    // Chamar API de logout no backend (opcional)
    await apiPost('/auth/logout', {}, token);
  }
  
  // Remover token e dados do usuário do localStorage
  removeAuthToken();
  
  return {
    success: true,
    message: 'Logout realizado com sucesso',
  };
}

/**
 * Obtém dados do usuário autenticado atual
 */
export async function getCurrentUser(): Promise<User | null> {
  const token = getAuthToken();
  
  if (!token) {
    return null;
  }

  const response = await apiGet<{ user: User }>('/auth/me', token);

  if (response.success && response.data?.user) {
    const user = response.data.user;
    setAuthUser(user); // Atualizar dados do usuário
    return user;
  }

  // Se a requisição falhar, remover token inválido
  removeAuthToken();
  return null;
}

