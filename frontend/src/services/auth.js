import api from './api';

const BASE = '/auth';

// Login - returns user object
export const login = (credentials) => 
  api.post(`${BASE}/login`, credentials).then(res => res.data);

// Register - returns user object
export const register = (userData) => 
  api.post(`${BASE}/register`, userData).then(res => res.data);

// Store current user in localStorage
export const saveUserToStorage = (user) => {
  if (user) {
    localStorage.setItem('currentUser', JSON.stringify(user));
  } else {
    localStorage.removeItem('currentUser');
  }
};

export const loadUserFromStorage = () => {
  const user = localStorage.getItem('currentUser');
  return user ? JSON.parse(user) : null;
};

export const clearUserFromStorage = () => {
  localStorage.removeItem('currentUser');
};