import { useState, useEffect } from 'react';
import { Curso } from '@/lib/types';
import { listarCursosSugeridos } from '@/lib/services/cursos.service';
import { atualizarProgressoCurso, atualizarProgressoCursoAlura, marcarCursoComoConcluido, marcarCursoAluraComoConcluido } from '@/lib/services/progresso.service';
import { criarCursoManual } from '@/lib/services/cursos.service';

interface SkillCardProps {
  id: number;
  title: string;
  category: string;
  level: 'Iniciante' | 'Intermediário' | 'Avançado' | 'Expert';
  progress: number;
  icon: string;
  color: string;
}

export default function SkillCard({ 
  id,
  title, 
  category, 
  level, 
  progress, 
  icon, 
  color,
}: SkillCardProps) {
  // Garantir que id está definido
  if (!id) {
    console.error('SkillCard: id é obrigatório');
    return null;
  }
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [courses, setCourses] = useState<Curso[]>([]);
  const [isLoadingSuggestions, setIsLoadingSuggestions] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleStudyClick = async () => {
    setIsModalOpen(true);
    setIsLoadingSuggestions(true);
    setError(null);
    
    try {
      // Buscar cursos sugeridos da API
      const cursosSugeridos = await listarCursosSugeridos(id);
      setCourses(cursosSugeridos);
    } catch (err) {
      console.error('Erro ao buscar cursos sugeridos:', err);
      setError('Erro ao carregar cursos sugeridos. Tente novamente.');
      setCourses([]);
    } finally {
      setIsLoadingSuggestions(false);
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setCourses([]);
  };

  const toggleCourseCompletion = async (course: Curso) => {
    try {
      const novoStatus = !course.concluido;
      
      if (course.id) {
        // Curso local
        if (novoStatus) {
          await marcarCursoComoConcluido(course.id);
        } else {
          await atualizarProgressoCurso(course.id, { progressoPercentual: 0, concluido: false });
        }
      } else if (course.idAlura) {
        // Curso da Alura
        if (novoStatus) {
          await marcarCursoAluraComoConcluido(course.idAlura);
        } else {
          await atualizarProgressoCursoAlura(course.idAlura, { progressoPercentual: 0, concluido: false });
        }
      }
      
      // Atualizar estado local
      setCourses(prevCourses => 
        prevCourses.map(c => 
          (c.id === course.id || c.idAlura === course.idAlura)
            ? { ...c, concluido: novoStatus, progressoPercentual: novoStatus ? 100 : 0 }
            : c
        )
      );
    } catch (err) {
      console.error('Erro ao atualizar progresso do curso:', err);
      setError('Erro ao atualizar progresso. Tente novamente.');
    }
  };

  const addNewCourse = () => {
    const newCourse: Curso = {
      nome: '',
      categoria: category,
      duracaoHoras: 10,
      descricao: '',
      link: '',
      concluido: false,
      origem: 'MANUAL',
      progressoPercentual: 0
    };
    setCourses(prevCourses => [...prevCourses, newCourse]);
  };

  const updateCourse = (course: Curso, field: keyof Curso, value: string | number) => {
    setCourses(prevCourses =>
      prevCourses.map(c =>
        (c.id === course.id || c.idAlura === course.idAlura)
          ? { ...c, [field]: value }
          : c
      )
    );
  };
  
  const saveManualCourse = async (course: Curso) => {
    try {
      if (!course.nome || course.nome.trim() === '') {
        setError('Nome do curso é obrigatório');
        return;
      }
      
      const cursoSalvo = await criarCursoManual({
        nome: course.nome,
        categoria: course.categoria || category,
        link: course.link,
        descricao: course.descricao,
        duracaoHoras: course.duracaoHoras,
        nivel: course.nivel
      });
      
      // Substituir curso temporário pelo salvo
      setCourses(prevCourses =>
        prevCourses.map(c =>
          (c.nome === course.nome && !c.id && !c.idAlura)
            ? cursoSalvo
            : c
        )
      );
    } catch (err) {
      console.error('Erro ao salvar curso manual:', err);
      setError('Erro ao salvar curso. Tente novamente.');
    }
  };

  const toggleEditMode = (course: Curso) => {
    // Para cursos manuais, permitir edição
    if (course.origem === 'MANUAL' && !course.id && !course.idAlura) {
      // Curso ainda não salvo, apenas atualizar estado local
      return;
    }
    // Para cursos já salvos, não permitir edição por enquanto
    // (pode ser implementado depois com endpoint de atualização)
  };

  const deleteCourse = (course: Curso) => {
    // Remover apenas cursos manuais não salvos
    if (course.origem === 'MANUAL' && !course.id && !course.idAlura) {
      setCourses(prevCourses => 
        prevCourses.filter(c => 
          !(c.nome === course.nome && !c.id && !c.idAlura)
        )
      );
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
              <h2 className="modal-title">Relacionar Curso</h2>
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
                  <p>🤖 IA analisando e sugerindo cursos...</p>
                </div>
              ) : (
                <>
                  <div className="courses-list">
                    <div className="courses-header">
                      <h3 className="courses-title">Cursos Sugeridos pela IA</h3>
                      <button className="btn-add-course" onClick={addNewCourse}>
                        + Adicionar Curso
                      </button>
                    </div>
                    
                    {error && (
                      <div className="error-message" style={{ color: 'red', marginBottom: '1rem' }}>
                        {error}
                      </div>
                    )}
                    {courses.length === 0 && !isLoadingSuggestions ? (
                      <p className="no-courses">Nenhum curso sugerido ainda.</p>
                    ) : (
                      <div className="courses-grid">
                        {courses.map((course, index) => {
                          const courseKey = course.id || course.idAlura || `temp-${index}`;
                          const isEditing = course.origem === 'MANUAL' && !course.id && !course.idAlura;
                          
                          return (
                            <div 
                              key={courseKey} 
                              className={`course-item ${course.concluido ? 'completed' : ''} ${isEditing ? 'editing' : ''}`}
                            >
                              <div className="course-checkbox">
                                <input
                                  type="checkbox"
                                  id={`course-${courseKey}`}
                                  checked={course.concluido || false}
                                  onChange={() => toggleCourseCompletion(course)}
                                />
                                <label htmlFor={`course-${courseKey}`}></label>
                              </div>
                              
                              <div className="course-details">
                                {isEditing ? (
                                  <div className="course-edit-form">
                                    <input
                                      type="text"
                                      className="course-input"
                                      placeholder="Nome do curso"
                                      value={course.nome || ''}
                                      onChange={(e) => updateCourse(course, 'nome', e.target.value)}
                                    />
                                    <input
                                      type="url"
                                      className="course-input"
                                      placeholder="Link do curso (https://...)"
                                      value={course.link || ''}
                                      onChange={(e) => updateCourse(course, 'link', e.target.value)}
                                    />
                                    <input
                                      type="number"
                                      className="course-input-small"
                                      placeholder="Duração em horas"
                                      value={course.duracaoHoras || ''}
                                      onChange={(e) => updateCourse(course, 'duracaoHoras', parseInt(e.target.value) || 0)}
                                    />
                                    <button
                                      className="btn-save"
                                      onClick={() => saveManualCourse(course)}
                                    >
                                      Salvar
                                    </button>
                                  </div>
                                ) : (
                                  <>
                                    <h4 className="course-name">
                                      {course.link ? (
                                        <a href={course.link} target="_blank" rel="noopener noreferrer" className="course-link">
                                          {course.nome} 🔗
                                        </a>
                                      ) : (
                                        course.nome
                                      )}
                                    </h4>
                                    <p className="course-description">{course.descricao}</p>
                                    <span className="course-duration">
                                      ⏱️ {course.duracaoHoras ? `${course.duracaoHoras}h` : 'Duração não informada'}
                                    </span>
                                    {course.progressoPercentual !== undefined && course.progressoPercentual > 0 && (
                                      <div className="course-progress">
                                        <div className="progress-bar-small">
                                          <div 
                                            className="progress-fill-small" 
                                            style={{ width: `${course.progressoPercentual}%`, backgroundColor: color }}
                                          ></div>
                                        </div>
                                        <span className="progress-label">{course.progressoPercentual}% concluído</span>
                                      </div>
                                    )}
                                  </>
                                )}
                              </div>

                              <div className="course-actions">
                                {isEditing && (
                                  <button
                                    className="btn-icon btn-delete"
                                    onClick={() => deleteCourse(course)}
                                    title="Excluir"
                                  >
                                    🗑️
                                  </button>
                                )}
                                {course.concluido && !isEditing && (
                                  <div className="course-badge">
                                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                                      <path d="M16.667 5L7.5 14.167 3.333 10" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                    </svg>
                                  </div>
                                )}
                              </div>
                            </div>
                          );
                        })}
                      </div>
                    )}
                  </div>
                </>
              )}
            </div>

            <div className="modal-footer">
              <div className="courses-stats">
                <span className="stats-text">
                  {courses.filter(c => c.concluido).length} de {courses.length} concluídos
                </span>
              </div>
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
