'use client';

import { useState } from 'react';
import './UserProfileModal.css';

interface Skill {
  id: number;
  name: string;
  category: string;
  level: string;
  progress: number;
  icon: string;
  acquiredDate: string;
}

interface UserProfileModalProps {
  isOpen: boolean;
  onClose: () => void;
  userName: string;
  userEmail: string;
}

export default function UserProfileModal({ isOpen, onClose, userName, userEmail }: UserProfileModalProps) {
  const [monthlyGoal, setMonthlyGoal] = useState(10);
  const [isEditingGoal, setIsEditingGoal] = useState(false);
  const [tempGoal, setTempGoal] = useState(monthlyGoal);

  // Mock data - substituir por dados reais da API
  const acquiredSkills: Skill[] = [
    {
      id: 1,
      name: 'Machine Learning Fundamentals',
      category: 'Inteligência Artificial',
      level: 'Intermediário',
      progress: 100,
      icon: '🤖',
      acquiredDate: '2024-10-15'
    },
    {
      id: 2,
      name: 'React Avançado',
      category: 'Desenvolvimento Web',
      level: 'Avançado',
      progress: 100,
      icon: '⚛️',
      acquiredDate: '2024-09-20'
    },
    {
      id: 3,
      name: 'Python para Data Science',
      category: 'Análise de Dados',
      level: 'Intermediário',
      progress: 100,
      icon: '🐍',
      acquiredDate: '2024-08-10'
    },
    {
      id: 4,
      name: 'Docker e Containers',
      category: 'DevOps',
      level: 'Intermediário',
      progress: 100,
      icon: '🐳',
      acquiredDate: '2024-07-05'
    },
    {
      id: 5,
      name: 'Liderança Ágil',
      category: 'Gestão',
      level: 'Avançado',
      progress: 100,
      icon: '📈',
      acquiredDate: '2024-06-18'
    }
  ];

  const completedCourses = 8;
  const totalXP = 2450;

  const handleSaveGoal = () => {
    if (tempGoal > 0 && tempGoal <= 50) {
      setMonthlyGoal(tempGoal);
      setIsEditingGoal(false);
    }
  };

  const handleCancelEdit = () => {
    setTempGoal(monthlyGoal);
    setIsEditingGoal(false);
  };

  if (!isOpen) return null;

  return (
    <div className="profile-modal-overlay" onClick={onClose}>
      <div className="profile-modal-content" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="profile-modal-header">
          <div className="profile-header-info">
            <div className="profile-avatar-large">
              {userName.charAt(0).toUpperCase()}
            </div>
            <div className="profile-user-details">
              <h2 className="profile-user-name">{userName}</h2>
              <p className="profile-user-email">{userEmail}</p>
            </div>
          </div>
          <button className="profile-modal-close" onClick={onClose}>
            <svg width="24" height="24" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
              <path d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Stats */}
        <div className="profile-stats-section">
          <div className="profile-stat-card">
            <div className="profile-stat-icon" style={{ backgroundColor: '#167bf724' }}>
              <span style={{ color: '#167BF7' }}>🎯</span>
            </div>
            <div className="profile-stat-content">
              <h3 className="profile-stat-value">{acquiredSkills.length}</h3>
              <p className="profile-stat-label">Habilidades Adquiridas</p>
            </div>
          </div>
          <div className="profile-stat-card">
            <div className="profile-stat-icon" style={{ backgroundColor: '#00C86F24' }}>
              <span style={{ color: '#00C86F' }}>📚</span>
            </div>
            <div className="profile-stat-content">
              <h3 className="profile-stat-value">{completedCourses}</h3>
              <p className="profile-stat-label">Cursos Concluídos</p>
            </div>
          </div>
          <div className="profile-stat-card">
            <div className="profile-stat-icon" style={{ backgroundColor: '#FF6B0024' }}>
              <span style={{ color: '#FF6B00' }}>⭐</span>
            </div>
            <div className="profile-stat-content">
              <h3 className="profile-stat-value">{totalXP}</h3>
              <p className="profile-stat-label">XP Total</p>
            </div>
          </div>
        </div>

        {/* Monthly Goal */}
        <div className="profile-goal-section">
          <div className="profile-goal-header">
            <h3 className="profile-section-title">Meta Mensal de Cursos</h3>
            {!isEditingGoal && (
              <button className="profile-edit-btn" onClick={() => setIsEditingGoal(true)}>
                <svg width="16" height="16" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" />
                  <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" />
                </svg>
                Editar
              </button>
            )}
          </div>

          {isEditingGoal ? (
            <div className="profile-goal-edit">
              <div className="profile-input-group">
                <label htmlFor="goalInput" className="profile-input-label">
                  Número de cursos por mês:
                </label>
                <input
                  id="goalInput"
                  type="number"
                  min="1"
                  max="50"
                  value={tempGoal}
                  onChange={(e) => setTempGoal(parseInt(e.target.value) || 1)}
                  className="profile-goal-input"
                />
              </div>
              <div className="profile-goal-actions">
                <button className="profile-btn-cancel" onClick={handleCancelEdit}>
                  Cancelar
                </button>
                <button className="profile-btn-save" onClick={handleSaveGoal}>
                  Salvar Meta
                </button>
              </div>
            </div>
          ) : (
            <div className="profile-goal-display">
              <div className="profile-goal-info">
                <span className="profile-goal-current">{completedCourses}</span>
                <span className="profile-goal-separator">/</span>
                <span className="profile-goal-total">{monthlyGoal}</span>
                <span className="profile-goal-text">cursos este mês</span>
              </div>
              <div className="profile-progress-bar-container">
                <div className="profile-progress-bar-track">
                  <div 
                    className="profile-progress-bar-fill" 
                    style={{ width: `${Math.min((completedCourses / monthlyGoal) * 100, 100)}%` }}
                  >
                    <span className="profile-progress-percentage">
                      {Math.round((completedCourses / monthlyGoal) * 100)}%
                    </span>
                  </div>
                </div>
              </div>
              <p className="profile-goal-message">
                {completedCourses >= monthlyGoal
                  ? '🎉 Parabéns! Você atingiu sua meta!'
                  : `Faltam ${monthlyGoal - completedCourses} cursos para completar sua meta!`}
              </p>
            </div>
          )}
        </div>

        {/* Acquired Skills */}
        <div className="profile-skills-section">
          <h3 className="profile-section-title">Habilidades Adquiridas</h3>
          <div className="profile-skills-grid">
            {acquiredSkills.map((skill) => (
              <div key={skill.id} className="profile-skill-card">
                <div className="profile-skill-icon">{skill.icon}</div>
                <div className="profile-skill-details">
                  <h4 className="profile-skill-name">{skill.name}</h4>
                  <p className="profile-skill-category">{skill.category}</p>
                  <div className="profile-skill-meta">
                    <span className="profile-skill-level">{skill.level}</span>
                    <span className="profile-skill-date">
                      {new Date(skill.acquiredDate).toLocaleDateString('pt-BR')}
                    </span>
                  </div>
                </div>
                <div className="profile-skill-badge">
                  <svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                  </svg>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
