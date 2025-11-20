'use client';

import { useState } from 'react';
import UserProfileModal from './UserProfileModal';

export default function Header() {
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  // Mock data - substituir por dados reais do usuário logado
  const userName = "Luiz Gustavo";
  const userEmail = "luiz@example.com";

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

          <div className="profile" title='Perfil'>
            <button 
              onClick={() => setIsProfileOpen(true)} 
              className="profile-button"
              style={{ background: 'transparent', border: 'none', cursor: 'pointer', fontSize: '2rem' }}
            >
              🙋🏻‍♂️
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
