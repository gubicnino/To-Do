import React, { useEffect, useState } from 'react';
import { deleteTodo, getUserTodos } from '../../services/todos';
import { Link, useNavigate } from 'react-router-dom';
import './TodoList.css';
import { useUser } from '../../context/UserContext';


export default function TodoList() {
  const [todos, setTodos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const {currentUser} = useUser();
  const userId = currentUser?.id;
  const navigate = useNavigate();

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await getUserTodos(userId);
        setTodos(data);
      } catch (err) {
        setError(err.message || 'Failed to load');
      } finally {
        setLoading(false);
      }
    };
    if (userId) {
      load();
    }
  }, [userId]);  

  

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this todo?')) return;
    try {
      await deleteTodo(id);
      setTodos(prev => prev.filter(t => t.id !== id));
    } catch (err) {
      alert('Delete failed: ' + (err.message || err));
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div style={{color:'red'}}>Error: {error}</div>;

  return (
    <div className="todo-wrapper">
      <h2>Todos</h2>
      <button onClick={() => navigate('/todos/new')}>New Todo</button>
      {todos.length === 0 ? <p>No todos</p> : (
        <ul>
          {todos.map(t => (
            <li key={t.id}>
              <strong>Title:</strong> {t.title}
              <br />
              <strong>Description:</strong> {t.description}
              <br />
              <Link to={`/todos/${t.id}/edit`}>Edit</Link>{' '}
              <button onClick={() => handleDelete(t.id)}>Delete</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}