import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import TodoList from '../todos/TodoList';
import TodoForm from '../todos/TodoForm';

export default function Routing() {
  return (
    <Routes>
      {/* Redirect root to todos */}
      <Route path="/" element={<Navigate to="/todos" replace />} />
      
      {/* Todo routes */}
      <Route path="/todos" element={<TodoList />} />
      <Route path="/todos/new" element={<TodoForm />} />
      <Route path="/todos/:id/edit" element={<TodoForm />} />
      
      {/* 404 fallback */}
      <Route path="*" element={<div>Page not found</div>} />
    </Routes>
  );
}
