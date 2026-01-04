import React, { useEffect, useState } from 'react';
import { createTodo, getTodo, updateTodo } from '../../services/todos';
import { useNavigate, useParams } from 'react-router-dom';
import { useUser } from '../../context/UserContext';
import api from '../../services/api';
import './TodoForm.css';
import './Attachments.css';


export default function TodoForm() {
  const { id } = useParams(); 
  const isEdit = Boolean(id);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState('MEDIUM');
  const [dueDate, setDueDate] = useState('');
  const [completed, setCompleted] = useState(false);
  const [attachments, setAttachments] = useState([]);
  const [uploadingFiles, setUploadingFiles] = useState([]);
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
        setAttachments(data.attachments || []);
        
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

  const handleFileChange = (e) => {
    const files = Array.from(e.target.files);
    const newFiles = files.map(file => ({
      file,
      id: Date.now() + Math.random(),
      name: file.name,
      size: file.size,
      type: file.type,
      preview: file.type.startsWith('image/') ? URL.createObjectURL(file) : null,
      uploading: false
    }));
    setUploadingFiles(prev => [...prev, ...newFiles]);
    e.target.value = '';
  };

  const removeUploadingFile = (fileId) => {
    setUploadingFiles(prev => prev.filter(f => f.id !== fileId));
  };

  const removeAttachment = async (attachmentId) => {
    if (!window.confirm('Ali želiš izbrisati to prilogo?')) return;
    
    try {
      // TODO: FALI SE DELETE ENDPOINT NA BACKENDE
      await api.delete(`/todos/attachments/${attachmentId}`);
      setAttachments(prev => prev.filter(a => a.id !== attachmentId));
    } catch (err) {
      console.error('Failed to delete attachment:', err);
      alert('Ni uspelo izbrisati priloge. Poskusi ponovno.');
    }
  };

  const formatFileSize = (bytes) => {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  };

  const getFileIcon = (filename) => {
    const ext = filename.split('.').pop().toLowerCase();
    const iconMap = {
      pdf: '📄',
      doc: '📝', docx: '📝',
      xls: '📊', xlsx: '📊',
      ppt: '📽️', pptx: '📽️',
      txt: '📃',
      zip: '🗜️', rar: '🗜️',
      jpg: '🖼️', jpeg: '🖼️', png: '🖼️', gif: '🖼️', svg: '🖼️'
    };
    return iconMap[ext] || '📎';
  };

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

      let todoId;
      if (isEdit) {
        await updateTodo(id, todoData);
        todoId = id;
      } else {
        const newTodo = await createTodo(userId, todoData);
        todoId = newTodo.id;
      }

      if (uploadingFiles.length > 0) {
        for (const fileObj of uploadingFiles) {
          const formData = new FormData();
          formData.append('file', fileObj.file);
          
          try {
            const response = await api.post(`/todos/${todoId}/attachment`, formData, {
              headers: { 'Content-Type': 'multipart/form-data' }
            });
            
            // samo zacasnooo dotecas ka backend ne vrne attachment objekta
            const newAttachment = {
              id: Date.now() + Math.random(), // temporary ID
              filename: fileObj.name,
              fileSize: fileObj.size,
              fileType: fileObj.type,
              filePath: '' // backend te to zaopunE : NINO
            };
            setAttachments(prev => [...prev, newAttachment]);
            
          } catch (err) {
            console.error('Failed to upload file:', fileObj.name, err);
          }
        }
        setUploadingFiles([]);
      }

      if (!isEdit) {
        navigate('/todos');
      }
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

        <div className="form-group attachments-section">
          <label>📎 Attachments</label>
          
          <div className="upload-area">
            <input
              type="file"
              id="file-upload"
              multiple
              onChange={handleFileChange}
              className="file-input"
              accept="image/*,.pdf,.doc,.docx,.txt,.xls,.xlsx,.ppt,.pptx,.zip"
            />
            <label htmlFor="file-upload" className="upload-label">
              <div className="upload-icon">📤</div>
              <div className="upload-text">
                <strong>Click to upload</strong> or drag and drop
              </div>
              <div className="upload-hint">
                Images, PDFs, Documents (max 5MB each)
              </div>
            </label>
          </div>

          {uploadingFiles.length > 0 && (
            <div className="files-preview">
              <h4>Files to upload ({uploadingFiles.length})</h4>
              <div className="files-grid">
                {uploadingFiles.map(file => (
                  <div key={file.id} className="file-card uploading">
                    {file.preview ? (
                      <div className="file-preview-img" style={{ backgroundImage: `url(${file.preview})` }} />
                    ) : (
                      <div className="file-icon">{getFileIcon(file.name)}</div>
                    )}
                    <div className="file-info">
                      <div className="file-name" title={file.name}>{file.name}</div>
                      <div className="file-size">{formatFileSize(file.size)}</div>
                    </div>
                    <button
                      type="button"
                      className="remove-btn"
                      onClick={() => removeUploadingFile(file.id)}
                      title="Remove file"
                    >
                      ✕
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}

          {attachments.length > 0 && (
            <div className="files-preview">
              <h4>Existing attachments ({attachments.length})</h4>
              <div className="files-grid">
                {attachments.map(attachment => (
                  <div key={attachment.id} className="file-card">
                    {attachment.fileType?.startsWith('image/') ? (
                      <div 
                        className="file-preview-img" 
                        style={{ backgroundImage: `url(/api/v1/attachments/${attachment.id}/download)` }} 
                      />
                    ) : (
                      <div className="file-icon">{getFileIcon(attachment.filename)}</div>
                    )}
                    <div className="file-info">
                      <div className="file-name" title={attachment.filename}>{attachment.filename}</div>
                      <div className="file-size">{formatFileSize(attachment.fileSize)}</div>
                    </div>
                    <a
                      href={`/api/v1/attachments/${attachment.id}/download`}
                      className="download-btn"
                      title="Download"
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      ⬇
                    </a>
                    <button
                      type="button"
                      className="remove-btn"
                      onClick={() => removeAttachment(attachment.id)}
                      title="Delete attachment"
                    >
                      🗑️
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}
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