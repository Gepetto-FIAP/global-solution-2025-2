'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { removeAuthToken } from '@/lib/auth';
import UserProfileModal from './UserProfileModal';

export default function Header() {
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const router = useRouter();

  // Mock data - substituir por dados reais do usuário logado
  const userName = "Luiz Gustavo";
  const userEmail = "luiz@example.com";

  const handleLogout = () => {
    removeAuthToken();
    router.push('/auth/login');
  };

  return (
    <>
      <header className="header">
        <div className="header-container">
          <div className="logo">
            <span>
              Skill
            </span>
            <span>
              ify
            </span>
          </div>

          <div className="header-actions">
            <div className="profile" title='Perfil'>
              <button 
                onClick={() => setIsProfileOpen(true)} 
                className="profile-button"
                style={{ background: 'transparent', border: 'none', cursor: 'pointer', fontSize: '2rem' }}
              >
                🙋🏻‍♂️
              </button>
            </div>

            <button 
              onClick={handleLogout}
              className="logout-button"
              title="Sair"
            >
              <span className="logout-icon">🚪</span>
              <span className="logout-text">Sair</span>
            </button>
          </div>

        </div>
      </header>

      <UserProfileModal
        isOpen={isProfileOpen}
        onClose={() => setIsProfileOpen(false)}
        userName={userName}
        userEmail={userEmail}
      />
    </>
  );
}
