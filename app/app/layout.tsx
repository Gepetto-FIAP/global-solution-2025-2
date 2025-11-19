import Header from '../components/Header';
import Sidebar from '../components/Sidebar';
import './styles.css';

export default function AppLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="app-wrapper">
      <Header />
      <div className="app-container">
        <Sidebar />
        <main className="main-content">
          {children}
        </main>
      </div>
    </div>
  );
}
