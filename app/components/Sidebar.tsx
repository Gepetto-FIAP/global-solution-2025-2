'use client';

import { useState } from 'react';
import Link from 'next/link';

export default function Sidebar() {
  const [activeCategory, setActiveCategory] = useState('todas');

  const categories = [
    { id: 'todas', label: 'Todas', icon: '📚', count: 48, color: '#167BF7' },
    { id: 'tecnologia', label: 'Tecnologia', icon: '💻', count: 15, color: '#00C86F' },
    { id: 'design', label: 'Design', icon: '🎨', count: 12, color: '#9C27B0' },
    { id: 'negocios', label: 'Negócios', icon: '📈', count: 14, color: '#FF6B00' },
    { id: 'softskills', label: 'Soft Skills', icon: '💬', count: 11, color: '#01cafd' },
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h3 className="sidebar-title">Categorias</h3>
        <p className="sidebar-subtitle">Explore por área</p>
      </div>
      
      <nav className="sidebar-nav">
        {categories.map((category) => (
          <button
            key={category.id}
            className={`sidebar-item ${activeCategory === category.id ? 'active' : ''}`}
            onClick={() => setActiveCategory(category.id)}
          >
            <span 
              className="item-icon" 
              style={{ backgroundColor: `${category.color}20` }}
            >
              {category.icon}
            </span>
            <span className="item-label">{category.label}</span>
            <span 
              className="item-count"
              style={{ backgroundColor: `${category.color}20`, color: category.color }}
            >
              {category.count}
            </span>
          </button>
        ))}
      </nav>


    </aside>
  );
}
