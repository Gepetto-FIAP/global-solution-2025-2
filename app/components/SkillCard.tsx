import { useState, useEffect } from 'react';
import { AluraCourse } from '@/lib/types';
import { getAluraCursos } from '@/lib/services/alura.service';
import { enrollInCourse, updateEnrollmentProgress, completeCourse } from '@/lib/services/course.service';
import { getAuthUser } from '@/lib/auth';

interface SkillCardProps {
  title: string;
  category: string;
  level: 'Iniciante' | 'Intermediário' | 'Avançado' | 'Expert';
  progress: number;
  icon: string;
  color: string;
  categoriaSlug?: string;
  subcategoriaSlug?: string;
  skillId?: number;
}

export default function SkillCard({ 
  title, 
  category, 
  level, 
  progress, 
  icon, 
  color,
  categoriaSlug,
  subcategoriaSlug,
  skillId,
}: SkillCardProps) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [courses, setCourses] = useState<AluraCourse[]>([]);
  const [isLoadingSuggestions, setIsLoadingSuggestions] = useState(false);

  const handleStudyClick = async () => {
    setIsModalOpen(true);
    setIsLoadingSuggestions(true);
    
    try {
      // Buscar cursos reais da Alura baseados na categoria/subcategoria da skill
      const aluraCourses = await getAluraCursos(categoriaSlug, subcategoriaSlug);
      setCourses(aluraCourses);
    } catch (error) {
      console.error('Erro ao buscar cursos:', error);
    } finally {
      setIsLoadingSuggestions(false);
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setCourses([]);
  };

  const handleEnrollInCourse = async (course: AluraCourse) => {
    const user = getAuthUser();
    if (!user) {
      alert('Você precisa estar logado para se inscrever em um curso');
      return;
    }

    try {
      const enrollment = {
        idUsuario: user.id,
        idHabilidade: skillId,
        cursoSlug: course.slug,
        cursoNome: course.nome,
        tempoEstimado: course.tempoEstimado,
        horasEstudadas: 0,
        completado: false,
      };

      const result = await enrollInCourse(enrollment);
      
      if (result) {
        alert(`Você foi inscrito no curso: ${course.nome}`);
      } else {
        alert('Erro ao inscrever no curso. Você já pode estar inscrito.');
      }
    } catch (error) {
      console.error('Erro ao inscrever no curso:', error);
      alert('Erro ao inscrever no curso');
    }
  };

  const getLevelColor = (level: string) => {
    switch (level) {
      case 'Iniciante': return '#00C86F';
      case 'Intermediário': return '#167BF7';
      case 'Avançado': return '#9C27B0';
      case 'Expert': return '#FF6B00';
      default: return '#6C757D';
    }
  };

  return (
    <div className="skill-card">
      <div className="skill-card-header">
        <div className="skill-icon" style={{ backgroundColor: `${color}15` }}>
          <span style={{ color }}>{icon}</span>
        </div>
      </div>
      
      <div className="skill-content">
        <span className="skill-category">{category}</span>
        <h3 className="skill-title">{title}</h3>
        
        <div className="skill-level">
          <span 
            className="level-badge" 
            style={{ backgroundColor: `${getLevelColor(level)}15`, color: getLevelColor(level) }}
          >
            {level}
          </span>
        </div>

        <div className="skill-progress">
          <div className="progress-bar-small">
            <div 
              className="progress-fill-small" 
              style={{ width: `${progress}%`, backgroundColor: color }}
            ></div>
          </div>
          <span className="progress-label">{progress}% concluído</span>
        </div>
      </div>

      <div className="skill-footer">
        <button className="btn-skill" onClick={handleStudyClick}>
          Estudar
        </button>
      </div>

      {isModalOpen && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">Cursos Relacionados</h2>
              <button className="modal-close" onClick={handleCloseModal}>
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
              </button>
            </div>

            <div className="modal-body">
              <div className="skill-info">
                <div className="skill-info-item">
                  <span className="skill-info-label">Habilidade:</span>
                  <span className="skill-info-value">{title}</span>
                </div>
                <div className="skill-info-item">
                  <span className="skill-info-label">Categoria:</span>
                  <span className="skill-info-value">{category}</span>
                </div>
              </div>

              {isLoadingSuggestions ? (
                <div className="loading-suggestions">
                  <div className="spinner"></div>
                  <p>🔍 Buscando cursos da Alura...</p>
                </div>
              ) : (
                <>
                  <div className="courses-list">
                    <div className="courses-header">
                      <h3 className="courses-title">Cursos da Alura ({courses.length})</h3>
                    </div>
                    
                    {courses.length === 0 ? (
                      <p className="no-courses">Nenhum curso encontrado para esta categoria.</p>
                    ) : (
                      <div className="courses-grid">
                        {courses.map((course) => (
                          <div 
                            key={course.slug} 
                            className="course-item"
                          >
                            <div className="course-details">
                              <h4 className="course-name">{course.nome}</h4>
                              {course.descricao && (
                                <p className="course-description">{course.descricao}</p>
                              )}
                              <div className="course-meta">
                                <span className="course-duration">⏱️ {course.tempoEstimado}h</span>
                                {course.nivel && (
                                  <span className="course-level">{course.nivel}</span>
                                )}
                              </div>
                            </div>

                            <div className="course-actions">
                              <button
                                className="btn-enroll"
                                onClick={() => handleEnrollInCourse(course)}
                                title="Inscrever-se neste curso"
                              >
                                Inscrever-se
                              </button>
                            </div>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                </>
              )}
            </div>

            <div className="modal-footer">
              <button className="btn-cancel" onClick={handleCloseModal}>
                Fechar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
