import Link from 'next/link';
import './home.css';

export default function Home() {
  return (
    <div className="landing-page">
      {/* Navbar */}
      <nav className="navbar">
        <div className="nav-container">
          <div className="logo-landing">
            <span className="logo-part1">Skill</span>
            <span className="logo-part2">ify</span>
          </div>
          <div className="nav-links">
            <Link href="/auth/login" className="nav-link">Entrar</Link>
            <Link href="/auth/register" className="nav-button">Começar Grátis</Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-container">
          <div className="hero-badge">
            <span className="badge-icon">🚀</span>
            <span>Powered by AI</span>
          </div>
          <h1 className="hero-heading">
            Transforme Suas 
            <span className="highlight"> Habilidades </span>
            em Oportunidades
          </h1>
          <p className="hero-description">
            Plataforma inteligente que mapeia suas competências e conecta você aos melhores cursos da Alura para acelerar sua carreira no mercado de trabalho digital
          </p>
          <div className="hero-buttons">
            <Link href="/auth/register" className="btn-hero-primary">
              Começar Agora
              <svg width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </Link>
            <Link href="#features" className="btn-hero-secondary">
              <svg width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                <circle cx="12" cy="12" r="10"/>
                <path d="M10 8l6 4-6 4V8z"/>
              </svg>
              Ver Como Funciona
            </Link>
          </div>
          <div className="hero-stats">
            <div className="stat-box">
              <div className="stat-number">10+</div>
              <div className="stat-text">Categorias</div>
            </div>
            <div className="stat-box">
              <div className="stat-number">1000+</div>
              <div className="stat-text">Cursos Disponíveis</div>
            </div>
            <div className="stat-box">
              <div className="stat-number">24/7</div>
              <div className="stat-text">Suporte IA</div>
            </div>
          </div>
        </div>
        
        {/* Animated Background Elements */}
        <div className="hero-shapes">
          <div className="shape shape-1"></div>
          <div className="shape shape-2"></div>
          <div className="shape shape-3"></div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="features-section">
        <div className="features-container">
          <div className="section-header">
            <span className="section-badge">Recursos</span>
            <h2 className="section-heading">Por que escolher o Skillify?</h2>
            <p className="section-description">
              Tecnologia de ponta para impulsionar seu desenvolvimento profissional
            </p>
          </div>

          <div className="features-grid">
            <div className="feature-box">
              <div className="feature-icon-wrapper blue">
                <svg width="32" height="32" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
              </div>
              <h3 className="feature-heading">Mapeamento Inteligente</h3>
              <p className="feature-text">
                Organize e visualize todas as suas habilidades técnicas e comportamentais em um único lugar
              </p>
            </div>

            <div className="feature-box">
              <div className="feature-icon-wrapper purple">
                <svg width="32" height="32" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/>
                </svg>
              </div>
              <h3 className="feature-heading">Recomendações por IA</h3>
              <p className="feature-text">
                Inteligência artificial analisa seu perfil e sugere cursos personalizados da Alura
              </p>
            </div>

            <div className="feature-box">
              <div className="feature-icon-wrapper green">
                <svg width="32" height="32" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z"/>
                </svg>
              </div>
              <h3 className="feature-heading">Acompanhamento em Tempo Real</h3>
              <p className="feature-text">
                Monitore seu progresso, marque cursos concluídos e visualize sua evolução
              </p>
            </div>

            <div className="feature-box">
              <div className="feature-icon-wrapper orange">
                <svg width="32" height="32" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                </svg>
              </div>
              <h3 className="feature-heading">Biblioteca Alura</h3>
              <p className="feature-text">
                Acesso direto aos mais de 1000 cursos da maior escola de tecnologia do Brasil
              </p>
            </div>

            <div className="feature-box">
              <div className="feature-icon-wrapper cyan">
                <svg width="32" height="32" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                </svg>
              </div>
              <h3 className="feature-heading">Comunidade Ativa</h3>
              <p className="feature-text">
                Conecte-se com outros profissionais e compartilhe experiências de aprendizado
              </p>
            </div>

            <div className="feature-box">
              <div className="feature-icon-wrapper pink">
                <svg width="32" height="32" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/>
                </svg>
              </div>
              <h3 className="feature-heading">100% Seguro</h3>
              <p className="feature-text">
                Seus dados protegidos com criptografia de ponta e conformidade com LGPD
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="how-section">
        <div className="how-container">
          <div className="section-header">
            <span className="section-badge">Processo</span>
            <h2 className="section-heading">Como Funciona</h2>
          </div>

          <div className="steps-grid">
            <div className="step-card">
              <div className="step-number">01</div>
              <div className="step-icon">📝</div>
              <h3 className="step-title">Cadastre suas Habilidades</h3>
              <p className="step-description">
                Adicione suas competências técnicas e soft skills de forma rápida e organizada
              </p>
            </div>

            <div className="step-arrow">→</div>

            <div className="step-card">
              <div className="step-number">02</div>
              <div className="step-icon">🤖</div>
              <h3 className="step-title">IA Analisa e Recomenda</h3>
              <p className="step-description">
                Nossa IA processa seu perfil e sugere os cursos mais adequados para você
              </p>
            </div>

            <div className="step-arrow">→</div>

            <div className="step-card">
              <div className="step-number">03</div>
              <div className="step-icon">🚀</div>
              <h3 className="step-title">Evolua Continuamente</h3>
              <p className="step-description">
                Acompanhe seu progresso e alcance novos patamares na sua carreira
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="cta-container">
          <div className="cta-content">
            <h2 className="cta-heading">
              Pronto para Decolar sua Carreira?
            </h2>
            <p className="cta-text">
              Junte-se a milhares de profissionais que estão transformando suas habilidades em oportunidades reais
            </p>
            <Link href="/auth/register" className="btn-cta">
              Começar Gratuitamente
              <svg width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                <path d="M13 7l5 5m0 0l-5 5m5-5H6"/>
              </svg>
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <div className="footer-container">
          <div className="footer-brand">
            <div className="logo-landing">
              <span className="logo-part1">Skill</span>
              <span className="logo-part2">ify</span>
            </div>
            <p className="footer-tagline">Transformando habilidades em oportunidades</p>
          </div>
          <div className="footer-copy">
            <p>© 2025 Skillify. Todos os direitos reservados.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
