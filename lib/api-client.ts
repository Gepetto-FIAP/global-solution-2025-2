/**
 * Cliente HTTP para comunicação com backend Java REST API
 */

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
}

/**
 * Realiza uma requisição POST para o backend Java
 */
export async function apiPost<T>(
  endpoint: string,
  data: unknown,
  token?: string
): Promise<ApiResponse<T>> {
  try {
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method: 'POST',
      headers,
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      return {
        success: false,
        message: result.message || 'Erro ao processar requisição',
      };
    }

    return {
      success: true,
      data: result,
    };
  } catch (error) {
    console.error('API POST error:', error);
    return {
      success: false,
      message: error instanceof Error ? error.message : 'Erro de conexão com o servidor',
    };
  }
}

/**
 * Realiza uma requisição GET para o backend Java
 */
export async function apiGet<T>(
  endpoint: string,
  token?: string
): Promise<ApiResponse<T>> {
  try {
    const headers: HeadersInit = {};

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method: 'GET',
      headers,
    });

    const result = await response.json();

    if (!response.ok) {
      return {
        success: false,
        message: result.message || 'Erro ao processar requisição',
      };
    }

    return {
      success: true,
      data: result,
    };
  } catch (error) {
    console.error('API GET error:', error);
    return {
      success: false,
      message: error instanceof Error ? error.message : 'Erro de conexão com o servidor',
    };
  }
}

