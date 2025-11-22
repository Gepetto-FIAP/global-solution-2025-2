'use client';

import { useState, useEffect } from 'react';
import { AluraCategory } from '@/lib/types';
import { getAluraCategorias } from '@/lib/services/alura.service';

interface SidebarProps {
  onCategorySelect?: (categorySlug: string, categoryName: string) => void;
  activeCategory?: string;
}

export default function Sidebar({ onCategorySelect, activeCategory: externalActiveCategory }: SidebarProps) {
  const [internalActiveCategory, setInternalActiveCategory] = useState('todas');
  const [categories, setCategories] = useState<AluraCategory[]>([]);
  const [loading, setLoading] = useState(true);

  // Use external activeCategory if provided, otherwise use internal state
  const activeCategory = externalActiveCategory !== undefined ? externalActiveCategory : internalActiveCategory;

  useEffect(() => {
    async function loadCategories() {
      setLoading(true);
      const data = await getAluraCategorias();
      setCategories(data);
      setLoading(false);
    }

    loadCategories();
  }, []);

  // Função para gerar uma cor padrão baseada no nome se não houver cor definida
  const getCategoryColor = (category: AluraCategory, index: number) => {
    if (category.cor) return category.cor;
    
    const defaultColors = ['#167BF7', '#00C86F', '#9C27B0', '#FF6B00', '#01cafd', '#ffba05'];
    return defaultColors[index % defaultColors.length];
  };

  // Função para gerar um ícone baseado no nome da categoria
  const getCategoryIcon = (nome: string) => {
    const lowerNome = nome.toLowerCase();
    if (lowerNome.includes('mobile')) return '📱';
    if (lowerNome.includes('programação') || lowerNome.includes('programacao')) return '💻';
    if (lowerNome.includes('front')) return '🎨';
    if (lowerNome.includes('data') || lowerNome.includes('dados')) return '📊';
    if (lowerNome.includes('inteligência') || lowerNome.includes('inteligencia') || lowerNome.includes('ia')) return '🤖';
    if (lowerNome.includes('devops')) return '⚙️';
    if (lowerNome.includes('ux') || lowerNome.includes('design')) return '✨';
    if (lowerNome.includes('inovação') || lowerNome.includes('inovacao') || lowerNome.includes('gestão') || lowerNome.includes('gestao')) return '📈';
    return '📚';
  };

  const handleCategoryClick = (slug: string, name: string) => {
    setInternalActiveCategory(slug);
    if (onCategorySelect) {
      onCategorySelect(slug, name);
    }
  };

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h3 className="sidebar-title">Categorias</h3>
        <p className="sidebar-subtitle">Explore por área</p>
      </div>
      
      {loading ? (
        <div className="sidebar-loading">
          <p>Carregando categorias...</p>
        </div>
      ) : (
        <nav className="sidebar-nav">
          <button
            className={`sidebar-item ${activeCategory === 'todas' ? 'active' : ''}`}
            onClick={() => handleCategoryClick('todas', 'Todas as Categorias')}
          >
            <span 
              className="item-icon" 
              style={{ backgroundColor: '#167bf724' }}
            >
              📚
            </span>
            <span className="item-label">Todas</span>
            <span 
              className="item-count"
              style={{ backgroundColor: '#167bf724', color: '#167BF7' }}
            >
              {categories.reduce((acc, cat) => acc + (cat.numeroCursos || 0), 0)}
            </span>
          </button>

          {categories.map((category, index) => {
            const color = getCategoryColor(category, index);
            const icon = getCategoryIcon(category.nome);
            const courseCount = category.numeroCursos || 0;

            return (
              <button
                key={category.slug}
                className={`sidebar-item ${activeCategory === category.slug ? 'active' : ''}`}
                onClick={() => handleCategoryClick(category.slug, category.nome)}
              >
                <span 
                  className="item-icon" 
                  style={{ backgroundColor: `${color}24` }}
                >
                  {icon}
                </span>
                <span className="item-label">{category.nome}</span>
                <span 
                  className="item-count"
                  style={{ backgroundColor: `${color}24`, color: color }}
                >
                  {courseCount}
                </span>
              </button>
            );
          })}
        </nav>
      )}
    </aside>
  );
}
