import React, { useState } from 'react';
import { useUser } from '../../context/UserContext';
import { useNavigate } from 'react-router-dom';
import { login as loginService } from '../../services/auth';
import './LoginForm.css';

export default function LoginForm({ onSuccess }) {  // ← Accept prop
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');  // Add error state
  const [loading, setLoading] = useState(false);
  const { login } = useUser();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');  // Clear previous errors
    setLoading(true);
    
    try {
      const user = await loginService({ username, password });
      login(user);
      
      // Close modal if onSuccess was provided
      if (onSuccess) {
        onSuccess();  // ← Call the function from Navigation
      }
      
      navigate('/todos');  // Navigate to todos
    } catch (error) {
      console.error('Login failed:', error);
      setError(error.response?.data?.message || 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="LoginForm">
      {error && <div className="error-message">{error}</div>}
      
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <button type="submit" disabled={loading}>
        {loading ? 'Logging in...' : 'Login'}
      </button>
    </form>
  );
}