import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

/**
 * Middleware para proteger rotas autenticadas
 * 
 * Nota: Como o token JWT está armazenado no localStorage (lado do cliente),
 * a verificação completa de autenticação é feita no componente da página.
 * Este middleware apenas redireciona rotas protegidas se necessário.
 * 
 * Se o backend Java usar cookies httpOnly, o middleware pode verificar cookies aqui.
 */
export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;

  // Permitir acesso a rotas públicas
  if (pathname.startsWith('/auth')) {
    return NextResponse.next();
  }

  // Para rotas /app, a verificação de autenticação é feita no componente
  // O componente redireciona para /auth/login se não autenticado
  // Isso permite que o token seja verificado do localStorage no cliente

  return NextResponse.next();
}

export const config = {
  matcher: [
    /*
     * Match all request paths except for the ones starting with:
     * - api (API routes)
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico (favicon file)
     */
    '/((?!api|_next/static|_next/image|favicon.ico).*)',
  ],
};

