import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';
import LoginForm from './auth/LoginForm';
import Modal from './common/Modal';
import './Navigation.css';

export default function Navigation() {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useUser();
  const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);
  const handleAccountClick = (e) => {
    if (!isLoggedIn) {
      e.preventDefault();
      setIsLoginModalOpen(true);
    }
  };
  const handleLogout = () => {
    logout();
    navigate('/');
  };
  const handleCloseModal = () => {
    setIsLoginModalOpen(false);  // Close modal
  };

  return (
    <>
      <nav className="navbar">
        <div className="navbar-container">
          <Link to="/" className="navbar-brand">
            Todo App
          </Link>

          <ul className="navbar-menu">
            <li>
              <Link to="/" className="navbar-link">Home</Link>
            </li>
            {isLoggedIn && (
              <li>
                <Link to="/todos" className="navbar-link">Todos</Link>
              </li>
            )}
            {isLoggedIn ? (
              <>
                <li>
                  <Link to="/Analitika" className="navbar-link">Analitika</Link>
                </li>
                <li>
                  <button onClick={handleLogout} className="navbar-link logout-btn">
                    Logout
                  </button>
                  
                </li>
              </>
            ) : (
              <li>
                <button onClick={handleAccountClick} className="navbar-link logout-btn">
                    Login
                </button>
              </li>
            )}
          </ul>
        </div>
      </nav>

      <Modal isOpen={isLoginModalOpen} onClose={handleCloseModal}>
        <LoginForm onSuccess={handleCloseModal} />
      </Modal>
    </>
  );
}