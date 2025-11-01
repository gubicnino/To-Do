import api from './api';

const BASE = '/todos';

export const getTodos = () => api.get(BASE).then(res => res.data);
export const getTodo = (id) => api.get(`${BASE}/${id}`).then(res => res.data);
export const createTodo = (userId, payload) => 
  api.post(BASE, payload, { params: { userId } }).then(res => res.data);
export const updateTodo = (id, payload) => api.put(`${BASE}/${id}`, payload).then(res => res.data);
export const deleteTodo = (id) => api.delete(`${BASE}/${id}`).then(res => res.data);

export const getUserTodos = (userId) => api.get(`${BASE}/user/${userId}`).then(res => res.data);