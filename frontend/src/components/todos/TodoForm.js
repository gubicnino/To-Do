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
  const [priority, setPriority] = useState('MEDIUM');
  const [dueDate, setDueDate] = useState('');
  const [completed, setCompleted] = useState(false);
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
        setPriority(data.priority || 'MEDIUM');
        setCompleted(data.completed || false);
        
        // Format dueDate for datetime-local input
        if (data.dueDate) {
          const date = new Date(data.dueDate);
          const formatted = date.toISOString().slice(0, 16);
          setDueDate(formatted);
        }
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
      const todoData = {
        title,
        description,
        priority,
        completed,
        dueDate: dueDate ? new Date(dueDate).toISOString() : null
      };

      if (isEdit) {
        await updateTodo(id, todoData);
      } else {
        await createTodo(userId, todoData);
      }
      navigate('/todos');
    } catch (err) {
      alert('Save failed: ' + (err.message || err));
    }
  };

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="todo-form-wrapper">
      <h2>{isEdit ? 'Edit' : 'New'} Todo</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">Title *</label>
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
          <label htmlFor="description">Description *</label>
          <textarea 
            id="description"
            value={description} 
            onChange={e => setDescription(e.target.value)} 
            required
            placeholder="Enter todo description"
            rows="4"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="priority">Priority</label>
            <select 
              id="priority"
              value={priority}
              onChange={e => setPriority(e.target.value)}
            >
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
              <option value="URGENT">Urgent</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="dueDate">Due Date</label>
            <input 
              id="dueDate"
              type="datetime-local"
              value={dueDate}
              onChange={e => setDueDate(e.target.value)}
            />
          </div>
        </div>

        {isEdit && (
          <div className="form-group checkbox-group">
            <label>
              <input 
                type="checkbox"
                checked={completed}
                onChange={e => setCompleted(e.target.checked)}
              />
              <span>Mark as completed</span>
            </label>
          </div>
        )}

        <div className="button-group">
          <button type="submit" className="btn-primary">
            {isEdit ? 'Update' : 'Create'} Todo
          </button>
          <button type="button" className="btn-secondary" onClick={() => navigate('/todos')}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}