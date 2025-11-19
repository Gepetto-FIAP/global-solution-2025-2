/**
 * Cliente HTTP para comunicação com backend Java REST API
 */

const API_BASE_URL = (process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080') + '/api';

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
    const errorMessage = error instanceof Error ? error.message : 'Erro de conexão com o servidor';
    
    // Verificar se é um erro de conexão (servidor não disponível)
    if (errorMessage.includes('Failed to fetch') || errorMessage.includes('NetworkError')) {
      return {
        success: false,
        message: 'Não foi possível conectar ao servidor. Verifique se o backend está rodando em http://localhost:8080',
      };
    }
    
    return {
      success: false,
      message: errorMessage,
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

/**
 * Realiza uma requisição PUT para o backend Java
 */
export async function apiPut<T>(
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
      method: 'PUT',
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
    console.error('API PUT error:', error);
    return {
      success: false,
      message: error instanceof Error ? error.message : 'Erro de conexão com o servidor',
    };
  }
}

/**
 * Realiza uma requisição DELETE para o backend Java
 */
export async function apiDelete(
  endpoint: string,
  token?: string
): Promise<ApiResponse<void>> {
  try {
    const headers: HeadersInit = {};

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method: 'DELETE',
      headers,
    });

    if (response.status === 204) {
      return {
        success: true,
      };
    }

    const result = await response.json();

    if (!response.ok) {
      return {
        success: false,
        message: result.message || 'Erro ao processar requisição',
      };
    }

    return {
      success: true,
    };
  } catch (error) {
    console.error('API DELETE error:', error);
    return {
      success: false,
      message: error instanceof Error ? error.message : 'Erro de conexão com o servidor',
    };
  }
}

