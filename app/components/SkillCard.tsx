import { useState, useEffect } from 'react';

interface Course {
  id: number;
  name: string;
  duration: string;
  description: string;
  link?: string;
  completed: boolean;
  isManual?: boolean;
  isEditing?: boolean;
}

interface SkillCardProps {
  title: string;
  category: string;
  level: 'Iniciante' | 'Intermediário' | 'Avançado' | 'Expert';
  progress: number;
  icon: string;
  color: string;
}

export default function SkillCard({ 
  title, 
  category, 
  level, 
  progress, 
  icon, 
  color,
}: SkillCardProps) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [courses, setCourses] = useState<Course[]>([]);
  const [isLoadingSuggestions, setIsLoadingSuggestions] = useState(false);

  const handleStudyClick = async () => {
    setIsModalOpen(true);
    setIsLoadingSuggestions(true);
    
    // Simular chamada à API de IA para sugerir cursos
    // Em produção, isso seria uma chamada real à sua API/IA
    await new Promise(resolve => setTimeout(resolve, 1500));
    
    const suggestedCourses: Course[] = [
      {
        id: 1,
        name: `${title} - Fundamentos`,
        duration: '8h',
        description: `Aprenda os conceitos fundamentais de ${title}`,
        completed: false
      },
      {
        id: 2,
        name: `${title} na Prática`,
        duration: '12h',
        description: `Projetos práticos e aplicações reais de ${title}`,
        completed: false
      },
      {
        id: 3,
        name: `${title} Avançado`,
        duration: '16h',
        description: `Técnicas avançadas e otimizações em ${title}`,
        completed: false
      },
      {
        id: 4,
        name: `Certificação ${title}`,
        duration: '20h',
        description: `Prepare-se para certificações profissionais`,
        completed: false
      }
    ];
    
    setCourses(suggestedCourses);
    setIsLoadingSuggestions(false);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setCourses([]);
  };

  const toggleCourseCompletion = (courseId: number) => {
    setCourses(prevCourses => 
      prevCourses.map(course => 
        course.id === courseId 
          ? { ...course, completed: !course.completed }
          : course
      )
    );
  };

  const addNewCourse = () => {
    const newCourse: Course = {
      id: Date.now(),
      name: '',
      duration: '10h',
      description: '',
      link: '',
      completed: false,
      isManual: true,
      isEditing: true
    };
    setCourses(prevCourses => [...prevCourses, newCourse]);
  };

  const updateCourse = (courseId: number, field: keyof Course, value: string) => {
    setCourses(prevCourses =>
      prevCourses.map(course =>
        course.id === courseId
          ? { ...course, [field]: value }
          : course
      )
    );
  };

  const toggleEditMode = (courseId: number) => {
    setCourses(prevCourses =>
      prevCourses.map(course =>
        course.id === courseId
          ? { ...course, isEditing: !course.isEditing }
          : course
      )
    );
  };

  const deleteCourse = (courseId: number) => {
    setCourses(prevCourses => prevCourses.filter(course => course.id !== courseId));
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
                    
                    {courses.length === 0 ? (
                      <p className="no-courses">Nenhum curso sugerido ainda.</p>
                    ) : (
                      <div className="courses-grid">
                        {courses.map((course) => (
                          <div 
                            key={course.id} 
                            className={`course-item ${course.completed ? 'completed' : ''} ${course.isEditing ? 'editing' : ''}`}
                          >
                            <div className="course-checkbox">
                              <input
                                type="checkbox"
                                id={`course-${course.id}`}
                                checked={course.completed}
                                onChange={() => toggleCourseCompletion(course.id)}
                              />
                              <label htmlFor={`course-${course.id}`}></label>
                            </div>
                            
                            <div className="course-details">
                              {course.isEditing && course.isManual ? (
                                <div className="course-edit-form">
                                  <input
                                    type="text"
                                    className="course-input"
                                    placeholder="Nome do curso"
                                    value={course.name}
                                    onChange={(e) => updateCourse(course.id, 'name', e.target.value)}
                                  />
                                  <input
                                    type="url"
                                    className="course-input"
                                    placeholder="Link do curso (https://...)"
                                    value={course.link || ''}
                                    onChange={(e) => updateCourse(course.id, 'link', e.target.value)}
                                  />
                                  <input
                                    type="text"
                                    className="course-input-small"
                                    placeholder="Duração (ex: 8h)"
                                    value={course.duration}
                                    onChange={(e) => updateCourse(course.id, 'duration', e.target.value)}
                                  />
                                </div>
                              ) : (
                                <>
                                  <h4 className="course-name">
                                    {course.link ? (
                                      <a href={course.link} target="_blank" rel="noopener noreferrer" className="course-link">
                                        {course.name} 🔗
                                      </a>
                                    ) : (
                                      course.name
                                    )}
                                  </h4>
                                  <p className="course-description">{course.description}</p>
                                  <span className="course-duration">⏱️ {course.duration}</span>
                                </>
                              )}
                            </div>

                            <div className="course-actions">
                              {course.isManual && (
                                <>
                                  <button
                                    className="btn-icon"
                                    onClick={() => toggleEditMode(course.id)}
                                    title={course.isEditing ? 'Salvar' : 'Editar'}
                                  >
                                    {course.isEditing ? '✓' : '✏️'}
                                  </button>
                                  <button
                                    className="btn-icon btn-delete"
                                    onClick={() => deleteCourse(course.id)}
                                    title="Excluir"
                                  >
                                    🗑️
                                  </button>
                                </>
                              )}
                              {course.completed && !course.isEditing && (
                                <div className="course-badge">
                                  <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                                    <path d="M16.667 5L7.5 14.167 3.333 10" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                  </svg>
                                </div>
                              )}
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
              <div className="courses-stats">
                <span className="stats-text">
                  {courses.filter(c => c.completed).length} de {courses.length} concluídos
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
