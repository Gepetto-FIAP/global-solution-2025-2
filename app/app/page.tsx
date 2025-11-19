'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import SkillCard from '../components/SkillCard';
import CadastroHabilidadeModal from '../components/CadastroHabilidadeModal';
import { getAuthUser } from '@/lib/auth';
import { getCurrentUser } from '@/lib/services/auth.service';
import { User, Habilidade, DashboardStats } from '@/lib/types';
import { listarHabilidades } from '@/lib/services/habilidades.service';
import { obterEstatisticas } from '@/lib/services/dashboard.service';

export default function App() {
  const router = useRouter();
  const [user, setUser] = useState<User | null>(null);
  const [habilidades, setHabilidades] = useState<Habilidade[]>([]);
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    async function loadData() {
      // Tentar obter do localStorage primeiro (mais rápido)
      const cachedUser = getAuthUser();
      if (cachedUser) {
        setUser(cachedUser);
      }

      try {
        // Buscar usuário atualizado
        const currentUser = await getCurrentUser();
        if (currentUser) {
          setUser(currentUser);
          
          // Buscar habilidades e estatísticas em paralelo
          const [habilidadesData, statsData] = await Promise.all([
            listarHabilidades().catch(err => {
              console.error('Erro ao carregar habilidades:', err);
              return [];
            }),
            obterEstatisticas().catch(err => {
              console.error('Erro ao carregar estatísticas:', err);
              return null;
            })
          ]);
          
          setHabilidades(habilidadesData);
          setStats(statsData);
          
          // Abrir modal automaticamente se não houver habilidades
          if (habilidadesData.length === 0) {
            setIsModalOpen(true);
          }
        } else {
          // Se não conseguir obter usuário, redirecionar para login
          router.push('/auth/login');
        }
      } catch (error) {
        console.error('Error loading data:', error);
        router.push('/auth/login');
      } finally {
        setIsLoading(false);
      }
    }

    loadData();
  }, [router]);

  const handleHabilidadeCriada = async (novaHabilidade: Habilidade) => {
    // Adicionar nova habilidade à lista
    setHabilidades((prev) => [...prev, novaHabilidade]);
    
    // Recarregar estatísticas para atualizar contadores
    try {
      const statsData = await obterEstatisticas();
      setStats(statsData);
    } catch (err) {
      console.error('Erro ao atualizar estatísticas:', err);
    }
  };

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
                <h3 className="stat-value">{stats?.habilidadesAdquiridas || 0}</h3>
                <p className="stat-label">Habilidades Adquiridas</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#00C86F24' }}>
                <span style={{ color: '#00C86F' }}>📚</span>
              </div>
              <div className="stat-content">
                <h3 className="stat-value">{stats?.cursosConcluidos || 0}</h3>
                <p className="stat-label">Cursos Concluídos</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#FF6B0024' }}>
                <span style={{ color: '#FF6B00' }}>⭐</span>
              </div>
              <div className="stat-content">
                <h3 className="stat-value">{stats?.xpTotal?.toLocaleString('pt-BR') || 0}</h3>
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
              <span className="progress-current">{stats?.cursosConcluidosMes || 0}</span>
              <span className="progress-separator">/</span>
              <span className="progress-total">{stats?.metaCursosMensal || 10}</span>
            </div>
          </div>
          <div className="progress-bar-container">
            <div className="progress-bar-track">
              <div className="progress-bar-fill" style={{ width: `${stats?.percentualMetaMensal || 0}%` }}>
                <span className="progress-percentage">{stats?.percentualMetaMensal || 0}%</span>
              </div>
            </div>
          </div>
          {stats && (
            <p className="progress-goal-message">
              {stats.cursosConcluidosMes >= stats.metaCursosMensal
                ? 'Parabéns! Você completou sua meta mensal! 🎉'
                : `Faltam apenas ${stats.metaCursosMensal - stats.cursosConcluidosMes} curso(s) para completar sua meta! 🎯`}
            </p>
          )}
        </div>
      </section>

      <section className="section" >
        <div className="section-header">
          <h2 className="section-title">Suas habilidades</h2>
        </div>
        {habilidades.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '2rem' }}>
            <p style={{ color: '#666', marginBottom: '1rem' }}>
              Você ainda não possui habilidades cadastradas.
            </p>
            <button
              onClick={() => setIsModalOpen(true)}
              className="btn-primary"
              style={{ marginTop: '1rem' }}
            >
              Adicionar Primeira Habilidade 🚀
            </button>
          </div>
        ) : (
          <div className="skills-grid">
            {habilidades.map((habilidade) => {
              // Mapear categoria para ícone e cor
              const getIconAndColor = (categoria: string) => {
                const map: Record<string, { icon: string; color: string }> = {
                  'Tecnologia': { icon: '💻', color: '#167BF7' },
                  'Design': { icon: '🎨', color: '#9C27B0' },
                  'Soft Skill': { icon: '🤝', color: '#00C86F' },
                  'Negócios': { icon: '📈', color: '#FF6B00' },
                  'Marketing': { icon: '📢', color: '#E91E63' },
                };
                return map[categoria] || { icon: '📚', color: '#6C757D' };
              };
              
              const { icon, color } = getIconAndColor(habilidade.categoria);
              
              return (
                <SkillCard
                  key={habilidade.id}
                  id={habilidade.id}
                  title={habilidade.nome}
                  category={habilidade.categoria}
                  level={(habilidade.nivel || 'Iniciante') as 'Iniciante' | 'Intermediário' | 'Avançado'}
                  progress={habilidade.progressoPercentual}
                  icon={icon}
                  color={color}
                />
              );
            })}
          </div>
        )}
      </section>

      <CadastroHabilidadeModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onHabilidadeCriada={handleHabilidadeCriada}
      />
    </div>
  );
}
