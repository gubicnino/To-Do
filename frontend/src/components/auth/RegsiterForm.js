import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../../context/UserContext';
import { register as registerService } from '../../services/auth';
import './LoginForm.css';

export default function LoginForm() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const { login } = useUser();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');  // Clear previous errors
        setLoading(true);

        try {
            if (password !== confirmPassword) {
                setError('Passwords do not match');
                setLoading(false);
                return;
            }
            const user = await registerService({ username, email, password });
            login(user);
            navigate('/todos');
        } catch (error) {
            console.error('Registration failed:', error);
            setError(error.response?.data?.message || 'Invalid registration details');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="LoginFormContainer">
            <h2>Register</h2>
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
                    type="text"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                />

                <button type="submit" disabled={loading}>
                    {loading ? 'Registering...' : 'Register'}
                </button>
            </form>

        </div>
    );
}