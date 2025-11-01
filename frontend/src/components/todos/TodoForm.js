import React, { useEffect, useState } from 'react';
import { createTodo, getTodo, updateTodo } from '../../services/todos';
import { useNavigate, useParams } from 'react-router-dom';
import { useUser } from '../../context/UserContext';
import './TodoForm.css';


export default function TodoForm() {
  const { id } = useParams(); 
  const isEdit = Boolean(id);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const { currentUser } = useUser();
  const userId = currentUser?.id;
  const [loading, setLoading] = useState(isEdit);
  const navigate = useNavigate();

  useEffect(() => {
    if (!isEdit) return;
    (async () => {
      try {
        const data = await getTodo(id);
        setTitle(data.title || '');
        setDescription(data.description || '');
      } catch (err) {
        alert('Failed to load todo');
      } finally {
        setLoading(false);
      }
    })();
  }, [id, isEdit]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEdit) {
        await updateTodo(id, { title, description });
      } else {
        await createTodo(userId, { title, description });
      }
      navigate('/todos');
    } catch (err) {
      alert('Save failed: ' + (err.message || err));
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="todo-form-wrapper">
      <h2>{isEdit ? 'Edit' : 'New'} Todo</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input 
            id="title"
            type="text"
            value={title} 
            onChange={e => setTitle(e.target.value)} 
            required
            placeholder="Enter todo title"
          />
        </div>
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea 
            id="description"
            value={description} 
            onChange={e => setDescription(e.target.value)} 
            required
            placeholder="Enter todo description"
          />
        </div>
        <div className="button-group">
          <button type="submit">Save</button>
          <button type="button" onClick={() => navigate('/todos')}>Cancel</button>
        </div>
      </form>
    </div>
  );
}