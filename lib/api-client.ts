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
    console.error('Endpoint:', `${API_BASE_URL}${endpoint}`);
    
    let errorMessage = 'Erro de conexão com o servidor';
    
    if (error instanceof TypeError && error.message.includes('fetch')) {
      errorMessage = 'Não foi possível conectar ao servidor. Verifique sua conexão.';
    } else if (error instanceof Error) {
      errorMessage = error.message;
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
    console.error('Endpoint:', `${API_BASE_URL}${endpoint}`);
    
    let errorMessage = 'Erro de conexão com o servidor';
    
    if (error instanceof TypeError && error.message.includes('fetch')) {
      errorMessage = 'Não foi possível conectar ao servidor. Verifique sua conexão.';
    } else if (error instanceof Error) {
      errorMessage = error.message;
    }
    
    return {
      success: false,
      message: errorMessage,
    };
  }
}

