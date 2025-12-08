import React, { useContext, useEffect, useState } from 'react';
import { deleteTodo, getUserTodos, updateTodo } from '../../services/todos';
import { Link, useNavigate } from 'react-router-dom';
import './TodoList.css';
import { useUser } from '../../context/UserContext';
import html2pdf from"html2pdf.js";
import {useRef} from "react";


export default function TodoList() {
  const [todos, setTodos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filter, setFilter] = useState('all'); 
  const [sortBy, setSortBy] = useState('dueDate');
  const {currentUser} = useUser();
  const userId = currentUser?.id;
  const navigate = useNavigate();
  const pdfRef = useRef();

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

  const generatePDF = (todo) => {
    const element = document.createElement("div");

    element.innerHTML = `
      <h1 style="text-align: center; margin-bottom: 20px;">TODO</h1>
      <div style="margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px;">
        <h2 style="margin: 0 0 10px 0; color: #333;">${todo.title}</h2>
        <p style="margin: 5px 0;"><strong>Opis:</strong> ${todo.description || 'Ni opisa'}</p>
        <p style="margin: 5px 0;"><strong>Prioriteta:</strong> ${todo.priority}</p>
        <p style="margin: 5px 0;"><strong>Stanje:</strong> ${todo.completed ? '✓ Zaključeno' : '○ Aktivno'}</p>
        <p style="margin: 5px 0;"><strong>Rok:</strong> ${todo.dueDate ? new Date(todo.dueDate).toLocaleString() : 'Ni določen'}</p>
      </div>
    `;

    const options = {
      margin: 10,
      filename: `${todo.title}.pdf`,
      jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };
    
    html2pdf().from(element).set(options).save();

  };

  const generateAllPDF = () => {
    const element = document.createElement("div");
    
    element.innerHTML = `
      <h1 style="text-align: center; margin-bottom: 20px;">Moji TODO-ji</h1>
      <p style="text-align: center; margin-bottom: 30px;">Skupno: ${todos.length} | Aktivni: ${stats.active} | Zaključeni: ${stats.completed}</p>
      ${sortedTodos.map((todo, index) => `
        <div style="margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px;">
          <h2 style="margin: 0 0 10px 0; color: #333;">${index + 1}. ${todo.title}</h2>
          <p style="margin: 5px 0;"><strong>Opis:</strong> ${todo.description || 'Ni opisa'}</p>
          <p style="margin: 5px 0;"><strong>Prioriteta:</strong> ${todo.priority}</p>
          <p style="margin: 5px 0;"><strong>Stanje:</strong> ${todo.completed ? '✓ Zaključeno' : '○ Aktivno'}</p>
          <p style="margin: 5px 0;"><strong>Rok:</strong> ${todo.dueDate ? new Date(todo.dueDate).toLocaleString() : 'Ni določen'}</p>
        </div>
      `).join('')}
    `;

    const options = {
      margin: 10,
      filename: `Vsi_TODO-ji_${new Date().toISOString().split('T')[0]}.pdf`,
      jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };
    
    html2pdf().from(element).set(options).save();
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this todo?')) return;
    try {
      await deleteTodo(id);
      setTodos(prev => prev.filter(t => t.id !== id));
    } catch (err) {
      alert('Delete failed: ' + (err.message || err));
    }
  };

  const toggleComplete = async (todo) => {
    try {
      const updated = { ...todo, completed: !todo.completed };
      await updateTodo(todo.id, updated);
      setTodos(prev => prev.map(t => t.id === todo.id ? updated : t));
    } catch (err) {
      alert('Update failed: ' + (err.message || err));
    }
  };

  const getPriorityColor = (priority) => {
    const colors = {
      LOW: '#4CAF50',
      MEDIUM: '#2196F3',
      HIGH: '#FF9800',
      URGENT: '#F44336'
    };
    return colors[priority] || '#999';
  };

  const formatDueDate = (dueDate) => {
    if (!dueDate) return null;
    const date = new Date(dueDate);
    const now = new Date();
    const isOverdue = date < now;
    
    return {
      formatted: date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}),
      isOverdue
    };
  };

  // Filter todos
  const filteredTodos = todos.filter(todo => {
    if (filter === 'active') return !todo.completed;
    if (filter === 'completed') return todo.completed;
    return true;
  });

  const sortedTodos = [...filteredTodos].sort((a, b) => {
    if (a.completed !== b.completed) {
      return a.completed ? 1 : -1;
    }
    
    if (sortBy === 'priority') {
      const priorityOrder = { URGENT: 1, HIGH: 2, MEDIUM: 3, LOW: 4 };
      return (priorityOrder[a.priority] || 99) - (priorityOrder[b.priority] || 99);
    }
    if (sortBy === 'dueDate') {
      if (!a.dueDate) return 1;
      if (!b.dueDate) return -1;
      return new Date(a.dueDate) - new Date(b.dueDate);
    }
    return 0;
  });

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  const stats = {
    total: todos.length,
    active: todos.filter(t => !t.completed).length,
    completed: todos.filter(t => t.completed).length
  };

  return (
    <div className="todo-wrapper">
      <div className="todo-header">
        <h2>My Todos</h2>
        <div className="header-actions">
          <button className="btn-pdf-all" onClick={generateAllPDF} disabled={todos.length === 0}>
            📄 Izvozi vse v PDF
          </button>
          <button className="btn-new" onClick={() => navigate('/todos/new')}>
            + New Todo
          </button>
        </div>
      </div>

      <div className="todo-stats">
        <span className="stat">Total: <strong>{stats.total}</strong></span>
        <span className="stat">Active: <strong>{stats.active}</strong></span>
        <span className="stat">Completed: <strong>{stats.completed}</strong></span>
      </div>

      <div className="todo-controls">
        <div className="filter-group">
          <label>Filter:</label>
          <button 
            className={filter === 'all' ? 'active' : ''}
            onClick={() => setFilter('all')}
          >
            All
          </button>
          <button 
            className={filter === 'active' ? 'active' : ''}
            onClick={() => setFilter('active')}
          >
            Active
          </button>
          <button 
            className={filter === 'completed' ? 'active' : ''}
            onClick={() => setFilter('completed')}
          >
            Completed
          </button>
        </div>

        <div className="sort-group">
          <label>Sort by:</label>
          <select value={sortBy} onChange={e => setSortBy(e.target.value)}>
            <option value="dueDate">Due Date</option>
            <option value="priority">Priority</option>
          </select>
        </div>
      </div>

      {sortedTodos.length === 0 ? (
        <p className="no-todos">No todos found</p>
      ) : (
        <div className="todo-grid">
          {sortedTodos.map(todo => {
            const dueDateInfo = formatDueDate(todo.dueDate);
            return (
              <div key={todo.id} className={`todo-card ${todo.completed ? 'completed' : ''}`}>
                <div className="todo-card-header">
                  <div className="todo-checkbox">
                    <input 
                      type="checkbox"
                      checked={todo.completed}
                      onChange={() => toggleComplete(todo)}
                    />
                  </div>
                  <div 
                    className="todo-priority"
                    style={{ backgroundColor: getPriorityColor(todo.priority) }}
                    title={todo.priority}
                  >
                    {todo.priority}
                  </div>
                </div>

                <div className="todo-content">
                  <h3 className={todo.completed ? 'completed-text' : ''}>{todo.title}</h3>
                  <p className="todo-description">{todo.description}</p>
                  
                  {dueDateInfo && (
                    <div className={`todo-due-date ${dueDateInfo.isOverdue && !todo.completed ? 'overdue' : ''}`}>
                      📅 {dueDateInfo.formatted}
                      {dueDateInfo.isOverdue && !todo.completed && <span className="overdue-label"> OVERDUE</span>}
                    </div>
                  )}
                </div>

              <div className="todo-actions">
                <Link to={`/todos/${todo.id}/edit`} className="btn-edit">
                    Edit
                </Link>
              <button onClick={() => handleDelete(todo.id)} className="btn-delete">
                Delete
              </button>
              <button onClick={() => generatePDF(todo)} className="btn-pdf">
                Spremeni v PDF
              </button>
              </div>
              </div>
              
            );
          })}
        </div>
      )}
    </div>
  );
}