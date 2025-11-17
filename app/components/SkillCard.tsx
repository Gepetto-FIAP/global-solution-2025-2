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
        <button className="btn-skill">
          Estudar
        </button>
      </div>
    </div>
  );
}
