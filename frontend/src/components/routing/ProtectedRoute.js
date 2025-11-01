import React from 'react';
import { Navigate } from 'react-router-dom';
import { useUser } from '../../context/UserContext';

export default function ProtectedRoute({ children }) {
  const { isLoggedIn } = useUser();

  // If not logged in, redirect to home
  if (!isLoggedIn) {
    return <Navigate to="/" replace />;
  }

  // If logged in, render the protected component
  return children;
}