import React, { useContext, useEffect, useState } from 'react';
import { deleteTodo, getUserTodos, updateTodo } from '../../services/todos';
import { Link, useNavigate } from 'react-router-dom';
import './TodoList.css';
import { useUser } from '../../context/UserContext';
import api from '../../services/api';
import Modal from '../common/Modal';


export default function TodoList() {
  const [todos, setTodos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filter, setFilter] = useState('all'); 
  const [sortBy, setSortBy] = useState('dueDate');
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [todoToDelete, setTodoToDelete] = useState(null);
  const [errorModal, setErrorModal] = useState({ open: false, message: '' });
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

  const generatePDF = async (todoId) => {
    try {
      const response = await api.get(`/todos/${todoId}/pdf`, {
        responseType: 'blob'
      });
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `todo_${todoId}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setErrorModal({ open: true, message: 'PDF export failed: ' + (err.message || err) });
    }
  };

  const generateAllPDF = async () => {
    try {
      const response = await api.get(`/todos/user/${userId}/pdf`, {
        responseType: 'blob'
      });
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `vsi_todos_${userId}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setErrorModal({ open: true, message: 'PDF export failed: ' + (err.message || err) });
    }
  };

  const openDeleteModal = (todo) => {
    setTodoToDelete(todo);
    setDeleteModalOpen(true);
  };

  const closeDeleteModal = () => {
    setDeleteModalOpen(false);
    setTodoToDelete(null);
  };

  const confirmDelete = async () => {
    if (!todoToDelete) return;
    try {
      await deleteTodo(todoToDelete.id);
      setTodos(prev => prev.filter(t => t.id !== todoToDelete.id));
      closeDeleteModal();
    } catch (err) {
      setErrorModal({ open: true, message: 'Delete failed: ' + (err.message || err) });
    }
  };

  const toggleComplete = async (todo) => {
    try {
      const updated = { ...todo, completed: !todo.completed };
      await updateTodo(todo.id, updated);
      setTodos(prev => prev.map(t => t.id === todo.id ? updated : t));
    } catch (err) {
      setErrorModal({ open: true, message: 'Update failed: ' + (err.message || err) });
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

  const getRecurrenceIcon = (frequency) => {
    const icons = {
      DAILY: '🔄',
      WEEKLY: '📅',
      MONTHLY: '🗓️',
      NONE: ''
    };
    return icons[frequency] || '';
  };

  const getRecurrenceLabel = (frequency) => {
    const labels = {
      DAILY: 'Dnevno',
      WEEKLY: 'Tedensko',
      MONTHLY: 'Mesečno',
      NONE: ''
    };
    return labels[frequency] || '';
  };

  const calculateNextOccurrence = (dueDate, frequency) => {
    if (!dueDate || frequency === 'NONE') return null;
    
    const date = new Date(dueDate);
    const now = new Date();
    
    // If due date is in the future, that's the next occurrence
    if (date > now) return date;
    
    // Calculate next occurrence based on frequency
    const next = new Date(date);
    while (next < now) {
      if (frequency === 'DAILY') {
        next.setDate(next.getDate() + 1);
      } else if (frequency === 'WEEKLY') {
        next.setDate(next.getDate() + 7);
      } else if (frequency === 'MONTHLY') {
        next.setMonth(next.getMonth() + 1);
      }
    }
    
    return next;
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
            const nextOccurrence = todo.isRecurring ? calculateNextOccurrence(todo.dueDate, todo.recurrenceFrequency) : null;
            return (
              <div key={todo.id} className={`todo-card ${todo.completed ? 'completed' : ''} ${todo.isRecurring ? 'recurring' : ''}`}>
                <div className="todo-card-header">
                  <div className="todo-checkbox">
                    <input 
                      type="checkbox"
                      checked={todo.completed}
                      onChange={() => toggleComplete(todo)}
                    />
                  </div>
                  {todo.isRecurring && (
                    <div className="recurring-badge" title={`Ponavljajoča naloga: ${getRecurrenceLabel(todo.recurrenceFrequency)}`}>
                      {getRecurrenceIcon(todo.recurrenceFrequency)} {getRecurrenceLabel(todo.recurrenceFrequency)}
                    </div>
                  )}
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

                  {todo.isRecurring && nextOccurrence && (
                    <div className="next-occurrence">
                      ⏭️ Naslednji: {nextOccurrence.toLocaleDateString()} {nextOccurrence.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                    </div>
                  )}

                  {todo.attachments && todo.attachments.length > 0 && (
                    <div className="todo-attachments-preview">
                      <div className="attachments-count">
                        📎 {todo.attachments.length} {todo.attachments.length === 1 ? 'attachment' : 'attachments'}
                      </div>
                      <div className="attachments-thumbnails">
                        {todo.attachments.slice(0, 3).map(att => (
                          <div key={att.id} className="attachment-thumb" title={att.filename}>
                            {att.fileType?.startsWith('image/') ? (
                              <img src={`/api/v1/attachments/${att.id}/download`} alt={att.filename} />
                            ) : (
                              <span className="file-icon-small">📄</span>
                            )}
                          </div>
                        ))}
                        {todo.attachments.length > 3 && (
                          <div className="attachment-thumb more">
                            +{todo.attachments.length - 3}
                          </div>
                        )}
                      </div>
                    </div>
                  )}
                </div>

              <div className="todo-actions">
                <Link to={`/todos/${todo.id}/edit`} className="btn-edit">
                    Edit
                </Link>
              <button onClick={() => openDeleteModal(todo)} className="btn-delete">
                Delete
              </button>
              <button onClick={() => generatePDF(todo.id)} className="btn-pdf">
                Spremeni v PDF
              </button>
              </div>
              </div>
              
            );
          })}
        </div>
      )}

      <Modal isOpen={deleteModalOpen} onClose={closeDeleteModal}>
        <div className="delete-modal">
          <div className="delete-modal-icon">⚠️</div>
          <h3>Delete Todo?</h3>
          <p>
            Are you sure you want to delete <strong>"{todoToDelete?.title}"</strong>? 
            This action cannot be undone.
          </p>
          <div className="delete-modal-actions">
            <button className="btn-cancel" onClick={closeDeleteModal}>
              Cancel
            </button>
            <button className="btn-delete-confirm" onClick={confirmDelete}>
              Delete
            </button>
          </div>
        </div>
      </Modal>

      <Modal isOpen={errorModal.open} onClose={() => setErrorModal({ open: false, message: '' })}>
        <div className="error-modal">
          <div className="error-modal-icon">❌</div>
          <h3>Error</h3>
          <p>{errorModal.message}</p>
          <button className="btn-primary" onClick={() => setErrorModal({ open: false, message: '' })}>
            OK
          </button>
        </div>
      </Modal>
    </div>
  );
}