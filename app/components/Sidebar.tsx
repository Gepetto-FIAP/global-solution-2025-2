'use client';

import { useState } from 'react';

export default function Sidebar() {
  const [activeFilter, setActiveFilter] = useState('todas');

  const categories = [
    { id: 'todas', label: 'Todas as Habilidades', icon: '📚', count: 48 },
    { id: 'tecnologia', label: 'Tecnologia', icon: '💻', count: 15 },
    { id: 'dados', label: 'Análise de Dados', icon: '📊', count: 8 },
    { id: 'ia', label: 'Inteligência Artificial', icon: '🤖', count: 10 },
    { id: 'gestao', label: 'Gestão', icon: '📈', count: 7 },
    { id: 'comunicacao', label: 'Comunicação', icon: '💬', count: 8 },
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h3>Categorias</h3>
      </div>
      <nav className="sidebar-nav" style={{display: 'none'}}>
        {categories.map((category) => (
          <button
            key={category.id}
            className={`sidebar-item ${activeFilter === category.id ? 'active' : ''}`}
            onClick={() => setActiveFilter(category.id)}
          >
            <span className="item-icon">{category.icon}</span>
            <span className="item-label">{category.label}</span>
            <span className="item-count">{category.count}</span>
          </button>
        ))}
      </nav>
      <div className="sidebar-footer" style={{display: 'none'}}>
        <div className="progress-widget">
          <h4>Seu Progresso</h4>
          <div className="progress-stats">
            <div className="stat">
              <span className="stat-value">12</span>
              <span className="stat-label">Concluídas</span>
            </div>
            <div className="stat">
              <span className="stat-value">8</span>
              <span className="stat-label">Em progresso</span>
            </div>
          </div>
          <div className="progress-bar">
            <div className="progress-fill" style={{ width: '65%' }}></div>
          </div>
          <p className="progress-text">65% do seu objetivo</p>
        </div>
      </div>
    </aside>
  );
}
