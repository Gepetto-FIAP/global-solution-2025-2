'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '../components/Sidebar';
import SkillCard from '../components/SkillCard';
import SkillCardButton from '../components/SkillCardButton';
import CourseList from '../components/CourseList';
import { getAuthUser } from '@/lib/auth';
import { getCurrentUser } from '@/lib/services/auth.service';
import { getDashboardStats } from '@/lib/services/course.service';
import { getSkillsByUser } from '@/lib/services/skill.service';
import { User, DashboardStats, Skill } from '@/lib/types';

export default function App() {
  const router = useRouter();
  const [user, setUser] = useState<User | null>(null);
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [skills, setSkills] = useState<Skill[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  
  // Estado para controlar a visualização de cursos por categoria
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [selectedCategoryName, setSelectedCategoryName] = useState<string>('');
  const [viewMode, setViewMode] = useState<'dashboard' | 'courses'>('dashboard');

  useEffect(() => {
    async function loadData() {
      // Tentar obter do localStorage primeiro (mais rápido)
      const cachedUser = getAuthUser();
      if (cachedUser) {
        setUser(cachedUser);
      }

      // Buscar do backend para garantir dados atualizados
      try {
        const currentUser = await getCurrentUser();
        if (currentUser) {
          setUser(currentUser);
          
          // Carregar estatísticas do dashboard
          const dashboardStats = await getDashboardStats(currentUser.id);
          if (dashboardStats) {
            setStats(dashboardStats);
          }
          
          // Carregar habilidades do usuário
          const userSkills = await getSkillsByUser(currentUser.id);
          setSkills(userSkills);
        } else {
          // Se não conseguir obter usuário, redirecionar para login
          router.push('/auth/login');
        }
      } catch (error) {
        console.error('Error loading user data:', error);
        router.push('/auth/login');
      } finally {
        setIsLoading(false);
      }
    }

    loadData();
  }, [router]);

  const handleCategorySelect = (categorySlug: string, categoryName: string) => {
    setSelectedCategory(categorySlug);
    setSelectedCategoryName(categoryName);
    setViewMode('courses');
  };

  const handleBackToDashboard = () => {
    setViewMode('dashboard');
    setSelectedCategory(null);
    setSelectedCategoryName('');
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

  // Calcular progresso da meta mensal (exemplo: 10 cursos por mês)
  const metaMensal = 10;
  const cursosCompletosNoMes = stats?.cursosCompletos || 0;
  const progressoMeta = Math.min((cursosCompletosNoMes / metaMensal) * 100, 100);

  // Função para gerar cores baseadas no índice
  const getSkillColor = (index: number) => {
    const colors = ['#167BF7', '#00C86F', '#9C27B0', '#FF6B00', '#01cafd', '#ffba05'];
    return colors[index % colors.length];
  };

  // Função para gerar ícones baseados na categoria
  const getSkillIcon = (categoriaSlug: string) => {
    if (categoriaSlug.includes('programacao')) return '💻';
    if (categoriaSlug.includes('front')) return '🎨';
    if (categoriaSlug.includes('data')) return '📊';
    if (categoriaSlug.includes('ia') || categoriaSlug.includes('inteligencia')) return '🤖';
    if (categoriaSlug.includes('devops')) return '⚙️';
    if (categoriaSlug.includes('design') || categoriaSlug.includes('ux')) return '✨';
    if (categoriaSlug.includes('mobile')) return '📱';
    if (categoriaSlug.includes('gestao') || categoriaSlug.includes('inovacao')) return '📈';
    return '🎯';
  };

  // Renderizar vista de cursos por categoria
  if (viewMode === 'courses') {
    return (
      <>
        <Sidebar 
          onCategorySelect={handleCategorySelect}
          activeCategory={selectedCategory || 'todas'}
        />
        <main className="main-content">
          <div className="back-to-dashboard">
            <button className="btn-back" onClick={handleBackToDashboard}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M19 12H5M12 19l-7-7 7-7"></path>
              </svg>
              Voltar ao Dashboard
            </button>
          </div>
          
          <CourseList 
            categoriaSlug={selectedCategory}
            categoriaName={selectedCategoryName}
          />
        </main>
      </>
    );
  }

  // Renderizar dashboard normal
  return (
    <>
      <Sidebar 
        onCategorySelect={handleCategorySelect}
        activeCategory={'todas'}
      />
      <main className="main-content">
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
                <h3 className="stat-value">{stats?.totalHabilidades || 0}</h3>
                <p className="stat-label">Habilidades Cadastradas</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#00C86F24' }}>
                <span style={{ color: '#00C86F' }}>📚</span>
              </div>
              <div className="stat-content">
                <h3 className="stat-value">{stats?.cursosCompletos || 0}</h3>
                <p className="stat-label">Cursos Concluídos</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#FF6B0024' }}>
                <span style={{ color: '#FF6B00' }}>⭐</span>
              </div>
              <div className="stat-content">
                <h3 className="stat-value">{stats?.xpTotal || 0}</h3>
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
              <span className="progress-current">{cursosCompletosNoMes}</span>
              <span className="progress-separator">/</span>
              <span className="progress-total">{metaMensal}</span>
            </div>
          </div>
          <div className="progress-bar-container">
            <div className="progress-bar-track">
              <div className="progress-bar-fill" style={{ width: `${progressoMeta}%` }}>
                <span className="progress-percentage">{Math.round(progressoMeta)}%</span>
              </div>
            </div>
          </div>
          <p className="progress-goal-message">
            {cursosCompletosNoMes >= metaMensal 
              ? 'Parabéns! Você atingiu sua meta mensal! 🎉' 
              : `Faltam apenas ${metaMensal - cursosCompletosNoMes} cursos para completar sua meta! 🎯`}
          </p>
        </div>
      </section>

      <section className="section" >
        <div className="section-header">
          <h2 className="section-title">Suas habilidades</h2>
        </div>
        <div className="skills-grid">
          {skills.length > 0 ? (
            skills.map((skill, index) => {
              // Calcular progresso baseado em cursos completos vs total de cursos
              const totalCursos = skill.totalCursos || 0;
              const cursosCompletos = skill.cursosCompletos || 0;
              const progress = totalCursos > 0 ? Math.round((cursosCompletos / totalCursos) * 100) : 0;

              return (
                <SkillCard
                  key={skill.idHabilidade}
                  title={skill.nome}
                  category={skill.categoriaSlug}
                  level={skill.nivel}
                  progress={progress}
                  icon={getSkillIcon(skill.categoriaSlug)}
                  color={getSkillColor(index)}
                  categoriaSlug={skill.categoriaSlug}
                  subcategoriaSlug={skill.subcategoriaSlug}
                  skillId={skill.idHabilidade}
                />
              );
            })
          ) : (
            <p style={{ gridColumn: '1 / -1', textAlign: 'center', padding: '2rem', color: '#666' }}>
              Você ainda não tem habilidades cadastradas. Adicione sua primeira habilidade!
            </p>
          )}

          <SkillCardButton
            onClick={() => alert('Funcionalidade de adicionar habilidade em desenvolvimento')}
            label="Adicionar Habilidade"
            className="skill-card-button"
          />
        </div>
      </section>
    </div>
  </main>
  </>
  );
}
