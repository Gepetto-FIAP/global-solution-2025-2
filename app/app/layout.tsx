import Header from '../components/Header';
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
        {children}
      </div>
    </div>
  );
}
