'use client';

import { useState } from 'react';
import { criarHabilidade, HabilidadeRequest } from '@/lib/services/habilidades.service';
import { Habilidade } from '@/lib/types';

interface CadastroHabilidadeModalProps {
  isOpen: boolean;
  onClose: () => void;
  onHabilidadeCriada: (habilidade: Habilidade) => void;
}

export default function CadastroHabilidadeModal({
  isOpen,
  onClose,
  onHabilidadeCriada,
}: CadastroHabilidadeModalProps) {
  const [formData, setFormData] = useState<HabilidadeRequest>({
    nome: '',
    categoria: '',
    descricao: '',
    nivel: undefined,
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const categorias = ['Tecnologia', 'Design', 'Soft Skill', 'Negócios', 'Marketing'];
  const niveis: Array<'Iniciante' | 'Intermediário' | 'Avançado'> = ['Iniciante', 'Intermediário', 'Avançado'];

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value === '' ? undefined : value,
    }));
    // Limpar erro do campo quando o usuário começar a digitar
    if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
    setError(null);
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    // Validar nome
    if (!formData.nome || formData.nome.trim() === '') {
      newErrors.nome = 'Nome é obrigatório';
    } else if (formData.nome.length > 100) {
      newErrors.nome = 'Nome deve ter no máximo 100 caracteres';
    }

    // Validar categoria
    if (!formData.categoria || formData.categoria.trim() === '') {
      newErrors.categoria = 'Categoria é obrigatória';
    } else if (!categorias.includes(formData.categoria)) {
      newErrors.categoria = 'Categoria inválida';
    }

    // Validar descrição (se preenchida)
    if (formData.descricao && formData.descricao.length > 1000) {
      newErrors.descricao = 'Descrição deve ter no máximo 1000 caracteres';
    }

    // Validar nível (se preenchido)
    if (formData.nivel && !niveis.includes(formData.nivel)) {
      newErrors.nivel = 'Nível inválido';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);

    try {
      const habilidade = await criarHabilidade({
        nome: formData.nome.trim(),
        categoria: formData.categoria,
        descricao: formData.descricao?.trim() || undefined,
        nivel: formData.nivel,
      });

      // Resetar formulário
      setFormData({
        nome: '',
        categoria: '',
        descricao: '',
        nivel: undefined,
      });
      setErrors({});

      // Chamar callback para atualizar lista
      onHabilidadeCriada(habilidade);

      // Fechar modal
      onClose();
    } catch (err) {
      console.error('Erro ao criar habilidade:', err);
      setError(err instanceof Error ? err.message : 'Erro ao criar habilidade. Tente novamente.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleClose = () => {
    if (!isLoading) {
      setFormData({
        nome: '',
        categoria: '',
        descricao: '',
        nivel: undefined,
      });
      setErrors({});
      setError(null);
      onClose();
    }
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2 className="modal-title">Cadastrar Primeira Habilidade 🚀</h2>
          <button className="modal-close" onClick={handleClose} disabled={isLoading}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="modal-body">
            {error && (
              <div className="error-message" style={{ 
                background: 'rgba(255, 59, 48, 0.1)',
                border: '1px solid rgba(255, 59, 48, 0.3)',
                color: '#ff3b30',
                padding: '0.75rem 1rem',
                borderRadius: '8px',
                marginBottom: '1.5rem',
                fontSize: '0.9rem',
              }}>
                {error}
              </div>
            )}

            <div className="form-group">
              <label htmlFor="nome" className="form-label">
                Nome <span style={{ color: '#ff3b30' }}>*</span>
              </label>
              <input
                id="nome"
                name="nome"
                type="text"
                className={`form-input ${errors.nome ? 'form-input-error' : ''}`}
                value={formData.nome}
                onChange={handleChange}
                placeholder="Ex: Java, React, Comunicação..."
                maxLength={100}
                disabled={isLoading}
                required
              />
              {errors.nome && (
                <span className="form-error">{errors.nome}</span>
              )}
              <span className="form-hint">{formData.nome.length}/100 caracteres</span>
            </div>

            <div className="form-group">
              <label htmlFor="categoria" className="form-label">
                Categoria <span style={{ color: '#ff3b30' }}>*</span>
              </label>
              <select
                id="categoria"
                name="categoria"
                className={`form-select ${errors.categoria ? 'form-input-error' : ''}`}
                value={formData.categoria}
                onChange={handleChange}
                disabled={isLoading}
                required
              >
                <option value="">Selecione uma categoria</option>
                {categorias.map((cat) => (
                  <option key={cat} value={cat}>
                    {cat}
                  </option>
                ))}
              </select>
              {errors.categoria && (
                <span className="form-error">{errors.categoria}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="descricao" className="form-label">
                Descrição
              </label>
              <textarea
                id="descricao"
                name="descricao"
                className={`form-textarea ${errors.descricao ? 'form-input-error' : ''}`}
                value={formData.descricao || ''}
                onChange={handleChange}
                placeholder="Descreva brevemente esta habilidade..."
                maxLength={1000}
                rows={4}
                disabled={isLoading}
              />
              {errors.descricao && (
                <span className="form-error">{errors.descricao}</span>
              )}
              <span className="form-hint">{(formData.descricao?.length || 0)}/1000 caracteres</span>
            </div>

            <div className="form-group">
              <label htmlFor="nivel" className="form-label">
                Nível
              </label>
              <select
                id="nivel"
                name="nivel"
                className={`form-select ${errors.nivel ? 'form-input-error' : ''}`}
                value={formData.nivel || ''}
                onChange={handleChange}
                disabled={isLoading}
              >
                <option value="">Selecione um nível (opcional)</option>
                {niveis.map((nivel) => (
                  <option key={nivel} value={nivel}>
                    {nivel}
                  </option>
                ))}
              </select>
              {errors.nivel && (
                <span className="form-error">{errors.nivel}</span>
              )}
            </div>
          </div>

          <div className="modal-footer">
            <button
              type="button"
              className="btn-cancel"
              onClick={handleClose}
              disabled={isLoading}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="btn-primary"
              disabled={isLoading}
            >
              {isLoading ? 'Cadastrando...' : 'Cadastrar Habilidade'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

