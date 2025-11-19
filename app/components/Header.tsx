import Link from 'next/link';

export default function Header() {
  return (
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
          <Link href="/app" className="profile-button">
            🙋🏻‍♂️
          </Link>
        </div>

      </div>
    </header>
  );
}
