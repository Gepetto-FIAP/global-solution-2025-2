'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { login } from '@/lib/services/auth.service';
import styles from './login.module.css';

export default function LoginPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError(null); // Limpar erro ao digitar
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    // Validação básica
    if (!formData.email || !formData.password) {
      setError('Por favor, preencha todos os campos');
      setIsLoading(false);
      return;
    }

    try {
      const response = await login(formData.email, formData.password);

      if (response.success) {
        // Redirecionar para o dashboard
        router.push('/app');
      } else {
        setError(response.message || 'Erro ao realizar login');
      }
    } catch (error) {
      console.error('Login error:', error);
      setError('Erro ao realizar login. Tente novamente.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.loginBox}>
        <div className={styles.header}>
          <h1 className={styles.title}>Bem-vindo de volta! 👋</h1>
          <p className={styles.subtitle}>
            Entre na sua conta para continuar aprendendo
          </p>
        </div>

        {error && (
          <div className={styles.errorMessage}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.inputGroup}>
            <label htmlFor="email" className={styles.label}>
              Email
            </label>
            <input
              id="email"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className={styles.input}
              placeholder="seu@email.com"
              required
              disabled={isLoading}
            />
          </div>

          <div className={styles.inputGroup}>
            <label htmlFor="password" className={styles.label}>
              Senha
            </label>
            <input
              id="password"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className={styles.input}
              placeholder="Sua senha"
              required
              disabled={isLoading}
            />
          </div>

          <button
            type="submit"
            className={styles.submitButton}
            disabled={isLoading}
          >
            {isLoading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <div className={styles.footer}>
          <p>
            Não tem uma conta?{' '}
            <Link href="/auth/register" className={styles.link}>
              Cadastre-se aqui
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

