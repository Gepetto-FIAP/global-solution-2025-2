/**
 * Helpers para gerenciar autenticação no frontend
 * Token JWT será recebido do backend Java e armazenado no localStorage
 */

const TOKEN_KEY = 'catalogo_auth_token';
const USER_KEY = 'catalogo_auth_user';

/**
 * Salva o token JWT no localStorage
 */
export function setAuthToken(token: string): void {
  if (typeof window !== 'undefined') {
    localStorage.setItem(TOKEN_KEY, token);
  }
}

/**
 * Obtém o token JWT do localStorage
 */
export function getAuthToken(): string | null {
  if (typeof window !== 'undefined') {
    return localStorage.getItem(TOKEN_KEY);
  }
  return null;
}

/**
 * Remove o token JWT do localStorage
 */
export function removeAuthToken(): void {
  if (typeof window !== 'undefined') {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }
}

/**
 * Verifica se o usuário está autenticado
 */
export function isAuthenticated(): boolean {
  return getAuthToken() !== null;
}

/**
 * Salva dados do usuário no localStorage
 */
export function setAuthUser(user: { id: number; nome: string; email: string }): void {
  if (typeof window !== 'undefined') {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }
}

/**
 * Obtém dados do usuário do localStorage
 */
export function getAuthUser(): { id: number; nome: string; email: string } | null {
  if (typeof window !== 'undefined') {
    const userStr = localStorage.getItem(USER_KEY);
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch {
        return null;
      }
    }
  }
  return null;
}

