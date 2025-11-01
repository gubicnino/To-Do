import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../common';
import './Index.css';
import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import Modal from '../common/Modal';
import LoginForm from '../auth/LoginForm';

export default function Index() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);

  useEffect(() => {
    // Check if redirected from protected route
    if (searchParams.get('login') === 'required') {
      setIsLoginModalOpen(true);
      // Clean up URL
      setSearchParams({});
    }
  }, [searchParams, setSearchParams]);
   const handleCloseModal = () => {
    setIsLoginModalOpen(false);
  };

  return (
    <div className="landing-page">
      <section className="hero">
        <div className="hero-content">
          <h1>Organize Your Life</h1>
          <p className="hero-subtitle">
            The simplest way to manage your tasks and boost productivity
          </p>
          <div className="hero-buttons">
            <Button 
              variant="primary" 
              onClick={() => navigate('/todos')}
            >
              Get Started
            </Button>
            <Button 
              variant="secondary" 
              onClick={() => navigate('/pricing')}
            >
              View Pricing
            </Button>
          </div>
        </div>
      </section>

      <section className="features">
        <h2>Why Choose Our Todo App?</h2>
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">📝</div>
            <h3>Simple & Clean</h3>
            <p>Intuitive interface that gets out of your way and lets you focus on what matters.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">⚡</div>
            <h3>Lightning Fast</h3>
            <p>Create, edit, and delete tasks in seconds. No unnecessary complexity.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🔒</div>
            <h3>Secure</h3>
            <p>Your data is safe with industry-standard security practices.</p>
          </div>
        </div>
      </section>

      <section className="cta">
        <h2>Ready to Get Organized?</h2>
        <p>Start managing your tasks efficiently today</p>
        <Button 
          variant="primary" 
          size="large"
          onClick={() => navigate('/todos')}
        >
          Start Now - It's Free
        </Button>
      </section>
      <Modal isOpen={isLoginModalOpen} onClose={handleCloseModal}>
        <LoginForm onSuccess={handleCloseModal} />
      </Modal>
    </div>
  );
}