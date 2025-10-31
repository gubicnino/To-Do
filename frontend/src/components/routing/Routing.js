import React from 'react';
import { Routes, Route } from 'react-router-dom';
import TodoList from '../todos/TodoList';
import TodoForm from '../todos/TodoForm';
import Index from '../index/Index';

export default function Routing() {
  return (
    <Routes>
      {/* Redirect root to todos */}
      <Route path="/" element={<Index />} />
      
      {/* Todo routes */}
      <Route path="/todos" element={<TodoList />} />
      <Route path="/todos/new" element={<TodoForm />} />
      <Route path="/todos/:id/edit" element={<TodoForm />} />
      
      {/* 404 fallback */}
      <Route path="*" element={<div>Page not found</div>} />
    </Routes>
  );
}
