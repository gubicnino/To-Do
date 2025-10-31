import api from './api';

const BASE = '/users';

export const getUsers = () => api.get(BASE).then(res => res.data);
export const getUser = (id) => api.get(`${BASE}/${id}`).then(res => res.data);
export const createUser = (payload) => api.post(BASE, payload).then(res => res.data);
export const updateUser = (id, payload) => api.put(`${BASE}/${id}`, payload).then(res => res.data);
export const deleteUser = (id) => api.delete(`${BASE}/${id}`).then(res => res.data);