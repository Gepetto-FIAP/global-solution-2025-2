'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import SkillCard from '../components/SkillCard';
import SkillCardButton from '../components/SkillCardButton';
import { getAuthUser } from '@/lib/auth';
import { getCurrentUser } from '@/lib/services/auth.service';
import { User } from '@/lib/types';

export default function App() {
  const router = useRouter();
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function loadUser() {
      // Tentar obter do localStorage primeiro (mais rápido)
      const cachedUser = getAuthUser();
      if (cachedUser) {
        setUser(cachedUser);
        setIsLoading(false);
      }

      // Buscar do backend para garantir dados atualizados
      try {
        const currentUser = await getCurrentUser();
        if (currentUser) {
          setUser(currentUser);
        } else {
          // Se não conseguir obter usuário, redirecionar para login
          router.push('/auth/login');
        }
      } catch (error) {
        console.error('Error loading user:', error);
        router.push('/auth/login');
      } finally {
        setIsLoading(false);
      }
    }

    loadUser();
  }, [router]);

  if (isLoading) {
    return (
      <div className="dashboard" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <p>Carregando...</p>
      </div>
    );
  }

  if (!user) {
    return null; // Redirecionamento será feito pelo useEffect
  }

  return (
    <div className="dashboard">

      <section className='top-section'>
        <div className="greeting-section">
          <h1 className="greeting-title">Olá, {user.nome}! 👋</h1>
          <p className="greeting-subtitle">Bem-vindo de volta! </p>
        </div>

        <div className="stats-section">
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#167bf724' }}>
                <span style={{ color: '#167BF7' }}>🎯</span>
              </div>
              <div className="stat-content">
                <h3 className="stat-value">12</h3>
                <p className="stat-label">Habilidades Adquiridas</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#00C86F24' }}>
                <span style={{ color: '#00C86F' }}>📚</span>
              </div>
              <div className="stat-content">
                <h3 className="stat-value">8</h3>
                <p className="stat-label">Cursos Concluídos</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#FF6B0024' }}>
                <span style={{ color: '#FF6B00' }}>⭐</span>
              </div>
              <div className="stat-content">
                <h3 className="stat-value">2.450</h3>
                <p className="stat-label">XP Total</p>
              </div>
            </div>
          </div>
        </div>
      </section>


      <section className="progress-goal-section">
        <div className="progress-goal-card">
          <div className="progress-goal-header">
            <div>
              <h3 className="progress-goal-title">Meta Mensal de Cursos</h3>
              <p className="progress-goal-subtitle">Continue assim para alcançar sua meta!</p>
            </div>
            <div className="progress-goal-stats">
              <span className="progress-current">8</span>
              <span className="progress-separator">/</span>
              <span className="progress-total">10</span>
            </div>
          </div>
          <div className="progress-bar-container">
            <div className="progress-bar-track">
              <div className="progress-bar-fill" style={{ width: '80%' }}>
                <span className="progress-percentage">80%</span>
              </div>
            </div>
          </div>
          <p className="progress-goal-message">Faltam apenas 2 cursos para completar sua meta! 🎯</p>
        </div>
      </section>

      <section className="section" >
        <div className="section-header">
          <h2 className="section-title">Suas habilidades</h2>
        </div>
        <div className="skills-grid">
          <SkillCard
            title="Machine Learning Fundamentals"
            category="Inteligência Artificial"
            level="Intermediário"
            progress={65}
            icon="🤖"
            color="#167BF7"
            
          />
          <SkillCard
            title="Análise de Dados com Python"
            category="Análise de Dados"
            level="Intermediário"
            progress={40}
            icon="📊"
            color="#00C86F"
            
          />
          <SkillCard
            title="Liderança Ágil"
            category="Gestão"
            level="Avançado"
            progress={80}
            icon="📈"
            color="#9C27B0"
            
          />

          <SkillCardButton
            onClick={() => alert('Adicionar nova habilidade')}
            label="Adicionar Habilidade"
            className="skill-card-button"
          />
        </div>
      </section>


    </div>
  );
}
