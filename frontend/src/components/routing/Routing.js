import React from 'react';
import { Routes, Route } from 'react-router-dom';
import TodoList from '../todos/TodoList';
import TodoForm from '../todos/TodoForm';
import RegisterForm from '../auth/RegsiterForm';
import Index from '../index/Index';
import ProtectedRoute from './ProtectedRoute';

export default function Routing() {
  return (
    <Routes>
      {/* Redirect root to todos */}
      <Route path="/" element={<Index />} />
      
      {/* Todo routes */}
      <Route path="/todos" element={<ProtectedRoute><TodoList /></ProtectedRoute>} />
      <Route path="/todos/new" element={<ProtectedRoute><TodoForm /></ProtectedRoute>} />
      <Route path="/todos/:id/edit" element={<ProtectedRoute><TodoForm /></ProtectedRoute>} />

      {/* Auth routes */}
      <Route path="/register" element={<RegisterForm />} />

      {/* 404 fallback */}
      <Route path="*" element={<div>Page not found</div>} />
    </Routes>
  );
}
