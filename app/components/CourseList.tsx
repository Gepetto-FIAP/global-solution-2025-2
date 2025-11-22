'use client';

import { useState, useEffect } from 'react';
import { AluraCourse } from '@/lib/types';
import { getAluraCursos } from '@/lib/services/alura.service';
import { enrollInCourse } from '@/lib/services/course.service';
import { getAuthUser } from '@/lib/auth';

interface CourseListProps {
  categoriaSlug: string | null;
  categoriaName: string;
  subcategoriaSlug?: string;
}

export default function CourseList({ categoriaSlug, categoriaName, subcategoriaSlug }: CourseListProps) {
  const [courses, setCourses] = useState<AluraCourse[]>([]);
  const [loading, setLoading] = useState(true);
  const [enrollingCourse, setEnrollingCourse] = useState<string | null>(null);

  useEffect(() => {
    async function loadCourses() {
      setLoading(true);
      try {
        // Se categoriaSlug é "todas", buscar todos os cursos, senão filtrar por categoria
        const allCourses = await getAluraCursos(
          categoriaSlug && categoriaSlug !== 'todas' ? categoriaSlug : undefined,
          subcategoriaSlug
        );
        setCourses(allCourses);
      } catch (error) {
        console.error('Erro ao carregar cursos:', error);
      } finally {
        setLoading(false);
      }
    }

    loadCourses();
  }, [categoriaSlug, subcategoriaSlug]);

  const handleEnroll = async (course: AluraCourse) => {
    const user = getAuthUser();
    if (!user) {
      alert('Você precisa estar logado para se inscrever em um curso');
      return;
    }

    setEnrollingCourse(course.slug);
    
    try {
      const enrollment = {
        idUsuario: user.id,
        cursoSlug: course.slug,
        cursoNome: course.nome,
        tempoEstimado: course.tempoEstimado,
        horasEstudadas: 0,
        completado: false,
      };

      const result = await enrollInCourse(enrollment);
      
      if (result) {
        alert(`✅ Inscrição realizada com sucesso!\n\nCurso: ${course.nome}\nDuração: ${course.tempoEstimado}h`);
      } else {
        alert('Você já está inscrito neste curso ou ocorreu um erro.');
      }
    } catch (error) {
      console.error('Erro ao inscrever no curso:', error);
      alert('Erro ao inscrever no curso. Tente novamente.');
    } finally {
      setEnrollingCourse(null);
    }
  };

  const getLevelColor = (nivel?: string) => {
    if (!nivel) return '#6C757D';
    const lowerNivel = nivel.toLowerCase();
    if (lowerNivel.includes('iniciante')) return '#00C86F';
    if (lowerNivel.includes('intermediário') || lowerNivel.includes('intermediario')) return '#167BF7';
    if (lowerNivel.includes('avançado') || lowerNivel.includes('avancado')) return '#9C27B0';
    return '#6C757D';
  };

  if (loading) {
    return (
      <div className="course-list-container">
        <div className="course-list-header">
          <div className="breadcrumb">
            <span className="breadcrumb-item">Todas as categorias</span>
            <span className="breadcrumb-separator">›</span>
            <span className="breadcrumb-item active">{categoriaName}</span>
          </div>
          <h1 className="course-list-title">{categoriaName}</h1>
        </div>
        
        <div className="loading-courses">
          <div className="spinner"></div>
          <p>Carregando cursos...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="course-list-container">
      <div className="course-list-header">
        <div className="breadcrumb">
          <span className="breadcrumb-item">Todas as categorias</span>
          <span className="breadcrumb-separator">›</span>
          <span className="breadcrumb-item active">{categoriaName}</span>
        </div>
        <div className="course-list-header-content">
          <h1 className="course-list-title">{categoriaName}</h1>
          <span className="course-count">{courses.length} curso{courses.length !== 1 ? 's' : ''}</span>
        </div>
      </div>

      {courses.length === 0 ? (
        <div className="empty-state">
          <div className="empty-state-icon">📚</div>
          <h3 className="empty-state-title">Nenhum curso encontrado</h3>
          <p className="empty-state-text">
            Não foram encontrados cursos para esta categoria no momento.
          </p>
        </div>
      ) : (
        <div className="courses-grid">
          {courses.map((course) => (
            <div key={course.slug} className="course-card">
              <div className="course-card-content">
                <h3 className="course-card-title">{course.nome}</h3>
                
                {course.descricao && (
                  <p className="course-card-description">{course.descricao}</p>
                )}
                
                <div className="course-card-meta">
                  <div className="course-card-info">
                    <span className="course-duration">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                        <circle cx="12" cy="12" r="10"></circle>
                        <polyline points="12 6 12 12 16 14"></polyline>
                      </svg>
                      {course.tempoEstimado}h
                    </span>
                  </div>
                </div>
              </div>
              
              <div className="course-card-footer">
                <button
                  className="btn-enroll-course"
                  onClick={() => handleEnroll(course)}
                  disabled={enrollingCourse === course.slug}
                >
                  {enrollingCourse === course.slug ? (
                    <>
                      <span className="spinner-small"></span>
                      Inscrevendo...
                    </>
                  ) : (
                    <>
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                        <path d="M12 5v14M5 12h14"></path>
                      </svg>
                      Inscrever-se
                    </>
                  )}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

